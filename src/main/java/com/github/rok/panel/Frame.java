package com.github.rok.panel;

import com.github.rok.utils.JRangeSlider;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.function.Consumer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Frame {
    private JFrame frame;

    private JProgressBar memoryBar;
    private JProgressBar cpuBar;

    public Frame(MainPanel main, XChartPanel memoryChartPanel, XChartPanel cpuChartPanel, int width, int height) {
        frame = new JFrame("Escalonamento De Processos");
        frame.setResizable(false);

        frame.setLayout(new java.awt.GridLayout(1, 3));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Painel para posicionar os elementos
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("CONTROLES");
        label.setFont(new Font("sans-serif", Font.PLAIN, 30));
        JPanel titlePanel = new JPanel();
        titlePanel.add(label);
        titlePanel.setBackground(Color.decode("#393939"));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        titlePanel.setPreferredSize(new Dimension(width/2, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(titlePanel, gbc);



        // Adiciona o botão de atualização
        JButton updateButton = new JButton("Memory - ▶");
        updateButton.setPreferredSize(new Dimension(140, 50));
        updateButton.addActionListener(e -> {
            updateButton.setText(main.getMemory().pause() ? "Memory - ▶" : "Memory - ◼");
        });
        JButton startStop = new JButton("CPU - Click to Start");
        startStop.setPreferredSize(new Dimension(140, 50));
        startStop.addActionListener(e -> {
            if (!main.getCpu().isRunning()) {
                main.getCpu().setRunningProcess(main.getMemory().getFirstProcess(), 2);
                startStop.setText("CPU - ◼");
                main.updateMemoryChart();
                return;
            }
            startStop.setText(main.getCpu().pause() ? "CPU - ▶" : "CPU - ◼");
        });
        JButton example = new JButton("example");
        example.setPreferredSize(new Dimension(140, 50));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(updateButton);
        buttonsPanel.add(startStop);
        buttonsPanel.add(example);
        updateButton.setSelected(false);

        // FIM DOS BOTÕES

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridy = 1;
        gbcButtons.insets = new Insets(10, 10, 10, 10);
        panel.add(buttonsPanel, gbcButtons);

        addSeparator(panel, 2, width); // ALGORITMO & TEMPO DE SIMULAÇÃO

        addSubTitle(panel, 3, "Algoritmo & Tempo de Simulação");

        JPanel selectPanel = new JPanel();
        addAlgoritymTime(selectPanel);
        GridBagConstraints gbcViewList = new GridBagConstraints();
        gbcViewList.gridy = 4;
        panel.add(selectPanel, gbcViewList);

        addSeparator(panel, 5, width); // TAMANHO E VELOCIDADE DO PROCESSO

        addSubTitle(panel, 6, "Tamanho & Velocidade do Processo");
        JPanel processSelectPanel = new JPanel();
        addProcessProperties(processSelectPanel);
        GridBagConstraints gbcCPU = new GridBagConstraints();
        gbcCPU.gridy = 7;
        panel.add(processSelectPanel, gbcCPU);

        addSeparator(panel, 8, width);

        // Preenche o painel com os elementos
        gbc = new GridBagConstraints();
        gbc.gridy = 10;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JPanel(), gbc);

        panel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));


        // Adiciona os gráficos ao JFrame pt 2
        JPanel memoryPanel = new JPanel();
        memoryBar = new JProgressBar(0, 100);
        memoryBar.setStringPainted(true);
        memoryBar.setString("Próximo processo");
        memoryBar.setSize(width, 10);
        memoryPanel.setSize(width, height);
        memoryPanel.setLayout(new BoxLayout(memoryPanel, BoxLayout.Y_AXIS));
        memoryPanel.add(memoryBar);
        memoryPanel.add(memoryChartPanel);

        JPanel cpuPanel = new JPanel();
        cpuBar = new JProgressBar(0, 100);
        cpuBar.setStringPainted(true);
        cpuBar.setString("Próximo processo");
        cpuBar.setSize(width, 10);
        cpuPanel.setSize(width, height);
        cpuPanel.setLayout(new BoxLayout(cpuPanel, BoxLayout.Y_AXIS));
        cpuPanel.add(cpuBar);
        cpuPanel.add(cpuChartPanel);

        frame.add(memoryPanel);
        frame.add(panel);
        frame.add(cpuPanel);

        frame.pack();
        frame.setVisible(true);
    }


    private void addProcessProperties(@NotNull JPanel panel) {
        JSpinner spinnerProcessDelay = createSpinner(5, 1, 20, 1);
        JSpinner spinnerProcessMin = createSpinner(1, 1, 98, 1);
        JSpinner spinnerProcessMax = createSpinner(10, 2, 99, 1);
        spinnerProcessMin.addChangeListener(e -> {
            JSpinner spinner = (JSpinner) e.getSource();
            if ((int) spinner.getValue() >= (int) spinnerProcessMax.getValue())
                spinnerProcessMax.setValue((int) spinner.getValue() + 1);
        });
        spinnerProcessMax.addChangeListener(e -> {
            JSpinner spinner = (JSpinner) e.getSource();
            if ((int) spinner.getValue() <= (int) spinnerProcessMin.getValue())
                spinnerProcessMin.setValue((int) spinner.getValue() - 1);
        });

        panel.add(new JLabel("Delay"));
        panel.add(spinnerProcessDelay);
        panel.add(new JLabel("     "));
        panel.add(new JLabel("Max."));
        panel.add(spinnerProcessMax);
        panel.add(spinnerProcessMin);
        panel.add(new JLabel("Min."));
    }

    private JSpinner createSpinner(int initial, int min, int max, int step) {
        JSpinner spin = createSpinner(initial, min, max, step, e -> {
            JSpinner spinner = (JSpinner) e.getComponent();
            if (e.getWheelRotation() < 0) {
                if ((int) spinner.getValue() == max) {
                    spinner.setValue(min);
                    return;
                }
                spinner.setValue((int) spinner.getValue() + 1);
            } else {
                if ((int) spinner.getValue() == min) {
                    spinner.setValue(max);
                    return;
                }
                spinner.setValue((int) spinner.getValue() - 1);
            }
        });
        return spin;
    }

    private JSpinner createSpinner(int initial, int min, int max, int step, Consumer<MouseWheelEvent> action) {
        SpinnerModel model = new SpinnerNumberModel(initial, min, max, step);
        JSpinner spinner = new JSpinner(model);
        spinner.setToolTipText("Use o Scroll");
        spinner.setPreferredSize(new Dimension(50, 30));
        spinner.addMouseWheelListener(action::accept);
        return spinner;
    }
    private void addAlgoritymTime(JPanel panel) {
        JComboBox<String> cb = new JComboBox<String>(new String[] { "First Come First Served", "Shortest Job First", "Round Robin", "Priority" });
        panel.add(new JLabel("Algoritmo: "));
        panel.add(cb);
        cb.setVisible(true);

        JSpinner spinnerMinutos = createSpinner(0,0, 59, 1);

        SpinnerModel segundosModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner spinnerSegundos = new JSpinner(segundosModel);
        spinnerSegundos.setValue(30);
        spinnerSegundos.setToolTipText("Use o Scroll");
        spinnerSegundos.setPreferredSize(new Dimension(50, 30));
        spinnerSegundos.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                if ((int) spinnerSegundos.getValue() == 59) {
                    spinnerSegundos.setValue(0);
                    spinnerMinutos.setValue((int) spinnerMinutos.getValue() + 1);
                    return;
                }
                spinnerSegundos.setValue((int) spinnerSegundos.getValue() + 1);
            } else {
                if ((int) spinnerSegundos.getValue() == 0) {
                    if ((int) spinnerMinutos.getValue() == 0) return;
                    spinnerSegundos.setValue(59);
                    spinnerMinutos.setValue((int) spinnerMinutos.getValue() - 1);
                    return;
                }
                spinnerSegundos.setValue((int) spinnerSegundos.getValue() - 1);
            }
        });

        panel.add(spinnerMinutos);
        panel.add(new JLabel("Min."));
        panel.add(spinnerSegundos);
        panel.add(new JLabel("Sec."));
    }
    private void addSubTitle(JPanel panel, int y, String title) {
        JLabel viewListLabel = new JLabel(title);
        viewListLabel.setFont(new Font("sans-serif", Font.PLAIN, 16));
        JPanel viewListPanel = new JPanel();
        viewListPanel.add(viewListLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = y;
        panel.add(viewListPanel, gbc);
    }
    private void addSeparator (JPanel panel, int y, int width) {
        JPanel jSeparator = new JPanel();
        jSeparator.setBackground(Color.decode("#393939"));
        jSeparator.setPreferredSize(new Dimension(width/2, 1));
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridy = y;
        panel.add(jSeparator, gb);
    }
    public void updateMemoryBar(int value) {
        memoryBar.setValue(value);
    }

    public void updateCpuBar(int value) {
        cpuBar.setValue(value);
    }
    public JFrame get() {
        return frame;
    }
}
