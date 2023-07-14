package com.github.rok.panel;

import com.formdev.flatlaf.ui.FlatSpinnerUI;
import com.github.rok.Main;
import com.github.rok.MainPanel;
import com.github.rok.interfaces.IReport;
import com.github.rok.utils.JNumberLabelUI;
import com.github.rok.utils.Utils;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Rok, Pedro Lucas N M Machado created on 07/07/2023
 */
public class ReportPanel {

    private JSpinner throughput;
    private JSpinner queue;
    private JSpinner cpu;
    private JSpinner returnProcess;
    public ReportPanel(JPanel panel, MainPanel main, int gridy) {
        Utils.addSeparator(panel, (gridy += 1), main.getWindowWidth());
        Utils.addSubTitle(panel, (gridy += 1), "Relatórios");
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new GridLayout(2, 2));
        addAlgoritymTime(selectPanel);
        GridBagConstraints gbcViewList = new GridBagConstraints();
        gbcViewList.gridy = (gridy += 1);
        panel.add(selectPanel, gbcViewList);
    }

    private void addAlgoritymTime(JPanel panel) {

        throughput = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
        throughput.setEditor(new JSpinner.NumberEditor(throughput, "#0/min"));
        throughput.setPreferredSize(new Dimension(70, 30));
        throughput.setEnabled(false);
        throughput.setUI(new JNumberLabelUI());

        queue = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
        queue.setEditor(new JSpinner.NumberEditor(queue, "#0.0/sec"));
        queue.setPreferredSize(new Dimension(70, 30));
        queue.setEnabled(false);
        queue.setUI(new JNumberLabelUI());

        cpu = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
        cpu.setEditor(new JSpinner.NumberEditor(cpu, "#%"));
        cpu.setPreferredSize(new Dimension(60, 30));
        cpu.setEnabled(false);
        cpu.setUI(new JNumberLabelUI());

        returnProcess = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
        returnProcess.setEditor(new JSpinner.NumberEditor(returnProcess, "#0.0/sec"));
        returnProcess.setPreferredSize(new Dimension(70, 30));
        returnProcess.setEnabled(false);
        returnProcess.setUI(new JNumberLabelUI());

        JLabel throughtputLabel = new JLabel("T. de Vazão ");
        throughtputLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(throughtputLabel, BorderLayout.WEST);
        panel.add(throughput);
        panel.add(queue);
        panel.add(new JLabel("T. Médio de fila"));

        JLabel cpuLabel = new JLabel("T. de CPU Ativa ");
        cpuLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(cpuLabel, BorderLayout.WEST);
        panel.add(cpu);
        panel.add(returnProcess);
        panel.add(new JLabel("T. Médio p/ retorno"));
    }

    public void setThroughputValue(double value) {
        throughput.setValue(value);
    }
    public void setQueueValue(double value) {
        queue.setValue(value);
    }
    public void setCpuValue(double cpu) {
        this.cpu.setValue(cpu);
    }
    public void setReturnProcessValue(double value) {
        returnProcess.setValue(value);
    }
}
