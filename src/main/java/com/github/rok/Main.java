package com.github.rok;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;

import com.github.rok.os.Memory;
import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;

/*
 * @Author Rok, Pedro Lucas N M Machado created on 03/07/2023
 */
public class Main {

	private static MainPanel mainPanel;

	public static void main(String[] args) throws Exception {
		mainPanel = new MainPanel();
	}

	public static MainPanel getPanel() {
		return mainPanel;
	}
}
