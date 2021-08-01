package yetmorecode.f1mp;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.EditorFrame;

public class EditorApp {
	public static void main(String[] args) {
		try {
		    //UIManager.setLookAndFeel( new FlatLightLaf() );
		    UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch( Exception ex ) {
		    System.err.println( "Failed to initialize LaF" );
		}
		
		var frame = new EditorFrame();
		frame.setVisible(true);
	}
}
