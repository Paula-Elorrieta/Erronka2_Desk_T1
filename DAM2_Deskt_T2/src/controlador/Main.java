package controlador;

import java.awt.EventQueue;

import view.LoginV;

public class Main {

		public static void main(String[] args) {
			
			EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                	
	                    LoginV frame = new LoginV();
	                    frame.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
        }
	}
