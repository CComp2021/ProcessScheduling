package com.github.rok.panel;

import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.*;

/*
 * @author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Frame {
    private JFrame frame;

    public Frame(MainPanel main, XChartPanel memoryChartPanel, XChartPanel cpuChartPanel) {
        frame = new JFrame("Escalonamento De Processos");
        frame.setResizable(false);

        frame.setLayout(new java.awt.GridLayout(1, 3));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Painel para posicionar os elementos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adiciona o botão de atualização
        JButton updateButton = new JButton("Add Random");
        updateButton.setPreferredSize(new Dimension(120, 20));
        updateButton.addActionListener(e -> main.addRandomProcessToMemory());

        JButton removeFirstBtn = new JButton("Remove First");
        removeFirstBtn.setPreferredSize(new Dimension(120, 40));
        removeFirstBtn.addActionListener(e -> {
            main.getMemory().removeProcess(main.getMemory().getFirstProcess());
            main.updateMemoryChart();
        });
        JButton removeLastBtn = new JButton("Remove Last");
        removeLastBtn.setPreferredSize(new Dimension(120, 40));
        removeLastBtn.addActionListener(e -> {
            main.getMemory().removeProcess(main.getMemory().getLastProcess());
            main.updateMemoryChart();
        });
        JButton startStop = new JButton("Start/Stop");
        startStop.setPreferredSize(new Dimension(140, 50));
        startStop.addActionListener(e -> {
            if (!main.getCpu().isRunning()) {
                main.getCpu().setRunningProcess(main.getMemory().getFirstProcess(), 2);
                main.updateMemoryChart();
                return;
            }
            main.getCpu().pause();
        });


        panel.add(Box.createHorizontalGlue());
        panel.add(updateButton);
        panel.add(removeFirstBtn);
        panel.add(removeLastBtn);
        panel.add(startStop);
        panel.add(Box.createHorizontalGlue());

        // Adiciona os gráficos ao JFrame pt 2
        frame.add(memoryChartPanel);
        frame.add(panel);
        frame.add(cpuChartPanel);

        frame.pack();
        frame.setVisible(true);
    }

    public JFrame get() {
        return frame;
    }
}
