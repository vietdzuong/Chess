package main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import menu.menuFrame;

public class main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
                        if(info.getName().equals("Nimbus")){
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
					menuFrame frame = new menuFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
