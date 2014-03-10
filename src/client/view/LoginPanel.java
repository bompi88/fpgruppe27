package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import resources.AppConstants;

import model.EmployeeModel;

import controller.LoginCtrl;
import controller.MainCtrl;
import framework.Controller;

/**
 *
 */
public class LoginPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -6617272538650375547L;

	private Controller ctrl;
	
	private JTextField usernameField = new JTextField();
	private JTextField passwordField = new JPasswordField();
	private JButton loginButton = new JButton();
	private Color backgroundColor = AppConstants.LOGIN_BG_COLOR;
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public LoginPanel(Controller ctrl) {
		
		this.ctrl = ctrl;
		
		this.setPreferredSize(new Dimension(500, 300));
		
		JPanel wrapper = new JPanel();
		wrapper.setPreferredSize(new Dimension(300, 220));
		setBackground(backgroundColor);
		wrapper.setBackground(backgroundColor);
		
		BufferedImage resizedImage;
		JLabel picLabel = null;
		ImageIcon myPicture = null;
		try {
			BufferedImage image = ImageIO.read(getClass().getResource("/resources/deer.png"));
			resizedImage = resize(image,120,90);
			myPicture = new ImageIcon(resizedImage);
			picLabel = new JLabel(myPicture);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		((MainCtrl) ctrl.getMainCtrl()).setAppIcon(myPicture);
		
		usernameField.setPreferredSize(new Dimension(300,30));
		passwordField.setPreferredSize(new Dimension(300,30));
		loginButton.setText(AppConstants.LOG_IN_BUTTON_TEXT);
		
		wrapper.add(picLabel);
		wrapper.add(usernameField);
		wrapper.add(passwordField);
		wrapper.add(loginButton);
	
		add(wrapper);
		
		usernameField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireLogin();
				}
			}
		});
		passwordField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireLogin();
				}
			}
		});
		
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireLogin();
			}
			
		});
	}
	
	public void fireLogin() {
		((EmployeeModel)getCtrl().getModel()).setUsername(usernameField.getText());
		((EmployeeModel)getCtrl().getModel()).setPassword(passwordField.getText());
		((LoginCtrl)getCtrl()).login();
	}

	public Controller getCtrl() {
		return ctrl;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyChanged = evt.getPropertyName();
		EmployeeModel model = (EmployeeModel)ctrl.getModel();
		
		if (propertyChanged.equals("username")) {
			usernameField.setText(model.getUsername());
		} else if (propertyChanged.equals("password")) {
			passwordField.setText(model.getPassword());
		}
	}	
}
