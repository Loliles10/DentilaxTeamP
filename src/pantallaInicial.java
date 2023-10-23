import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.PreparedStatement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//BBDD
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//BBDD

// Pantalla Inicial - Inicio - Login

public class pantallaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel logoInicio;
	private JLabel logoLogin;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pantallaInicial frame = new pantallaInicial();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
 
	}

	/**
	 * JFrame de la Pantalla Inicial
	 */
	public pantallaInicial() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 1280, 720);
	            		
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    contentPane.setBackground(Color.WHITE);
	    setContentPane(contentPane);
	    contentPane.setLayout(null);
	    
	    // Logo en página de inicio
	    logoInicio = new JLabel("");
	    logoInicio.setHorizontalAlignment(SwingConstants.CENTER);
	    logoInicio.setBounds(370, 100, 500, 500);  
	    ImageIcon imagen = new ImageIcon(getClass().getResource("/logoDentilax.png"));
	    Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(logoInicio.getWidth(), logoInicio.getHeight(), Image.SCALE_DEFAULT));
	    logoInicio.setIcon(icono);
	    
	    logoInicio.addMouseListener((MouseListener) new MouseAdapter() {
	    	
	    	@Override
            public void mouseClicked(MouseEvent e) {
	    		 
	    		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	            logoInicio.setVisible(false);
	    		
                contentPane.remove(logoInicio);
                
                logoInicio.setVisible(false);
                 
                contentPane.repaint();
                
                // Logo en página de login
                logoLogin = new JLabel("");
                logoLogin.setHorizontalAlignment(SwingConstants.CENTER);
                logoLogin.setBounds(270, 100, 500, 500);  
                ImageIcon imagen = new ImageIcon(getClass().getResource("/logoDentilax.png"));
                
                Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(logoLogin.getWidth(), logoLogin.getHeight(), Image.SCALE_DEFAULT));
                logoLogin.setIcon(icono);
                logoLogin.setBounds(222, 100, 500, 500); 
                contentPane.add(logoLogin); 
                
            	// Formulario de Login
                JTextField usuarioTextField = new JTextField();
                usuarioTextField.setBounds(770, 250, 200, 30);
                contentPane.add(usuarioTextField);

                JPasswordField contraseniaTextField = new JPasswordField();
                contraseniaTextField.setBounds(770, 300, 200, 30);
                contentPane.add(contraseniaTextField);

                // Agregar el JCheckBox debajo de los campos de contraseña
                JCheckBox recordarCheckBox = new JCheckBox("Recordar mi contraseña");
                recordarCheckBox.setBounds(770, 350, 200, 30);
                recordarCheckBox.setOpaque(false);
                contentPane.add(recordarCheckBox);
                
                // Botón Login #008CCE
                JButton miBoton = new JButton("Login");
                miBoton.setBounds(770, 400, 200, 40); 
                miBoton.setBackground(new Color(0, 140, 206)); 
                miBoton.setForeground(Color.WHITE); 
                contentPane.add(miBoton);
                
                // Comprobar si hay datos, sino sale en rojo
                miBoton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String usuario = usuarioTextField.getText();
                        char[] contrasenia = contraseniaTextField.getPassword();

                        if (usuario.isEmpty()) {
                            usuarioTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
                        } else {
                            usuarioTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        }

                        if (contrasenia.length == 0) {
                            contraseniaTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
                        } else {
                            contraseniaTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        }

                        boolean conexionExitosa = conectarABaseDeDatos();

                        if (conexionExitosa) {
                            JOptionPane.showMessageDialog(contentPane, "Conexión a la base de datos exitosa");

                            boolean credencialesValidas = verificarCredencialesEnBaseDeDatos(usuario, new String(contrasenia));

                            if (credencialesValidas) {
                                // Oculta la ventana actual
                                setVisible(false);

                                // Crea una instancia de la ventana principal
                                ventanaPrincipal ventana = new ventanaPrincipal();

                                // Establece operaciones de cierre para la ventana principal
                                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                                // Haz que la ventana principal sea visible
                                ventana.setVisible(true);
                            } else {
                                // Credenciales incorrectas, muestra un mensaje de error
                                JOptionPane.showMessageDialog(contentPane, "Credenciales incorrectas", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "No se pudo conectar a la base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                        }
                    }  

                    // Conexión a Base de Datos
					private boolean conectarABaseDeDatos() {
						
					    try {
					    	String url = "jdbc:mysql://localhost:3306/sys?useSSL=false";
						    String usuario = "root";
						    String contrasenia = "1234";

						    Connection conexion = null; 
					        conexion = DriverManager.getConnection(url, usuario, contrasenia);
					        if (conexion != null) {
					            conexion.close();
					        }
					        return true;
					    } catch (SQLException e) {
					        e.printStackTrace();
					        return false;
					    } 
					} 

					// prueba verificar credenciales
					public boolean verificarCredencialesEnBaseDeDatos(String usuario, String contrasenia) {
					    Connection connection = null;
					    PreparedStatement preparedStatement = null;
					    ResultSet resultSet = null;
					    boolean credencialesValidas = false;

					    try {
					        // Establecer la conexión a la base de datos (debes configurar esto según tu base de datos)
					        String url = "jdbc:mysql://localhost:3306/sys?useSSL=false";
					        String dbUser = "root";
					        String dbPassword = "1234";
					        connection = DriverManager.getConnection(url, dbUser, dbPassword);

					        // Consulta SQL para verificar las credenciales del usuario
					        String sql = "SELECT * FROM usuario WHERE Nombre = ? AND contraseña = ?";
					        preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
					        preparedStatement.setString(1, usuario);
					        preparedStatement.setString(2, contrasenia);
					        resultSet = preparedStatement.executeQuery(); 

					        // Si se encuentra un registro, las credenciales son válidas
					        credencialesValidas = resultSet.next();

					        if (credencialesValidas) {
					            // Credenciales correctas, muestra un mensaje de bienvenida
					            JOptionPane.showMessageDialog(null, "Bienvenido", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
					        } else {
					            // Credenciales incorrectas, muestra un mensaje de error
					            JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
					        }
					    } catch (SQLException e) {
					        e.printStackTrace();
					    } finally {
					        try {
					            if (resultSet != null) {
					                resultSet.close();
					            }
					            if (preparedStatement != null) {
					                preparedStatement.close();
					            }
					            if (connection != null) {
					                connection.close();
					            }
					        } catch (SQLException e) {
					            e.printStackTrace();
					        }
					    }

					    return credencialesValidas;
					}
					
					// Hints de Botones
	                TextPrompt usuario = new TextPrompt("Usuario", usuarioTextField); // hint
	                TextPrompt contrasenia = new TextPrompt("Contrasenia", contraseniaTextField); // hint
					
                });
                
                contentPane.repaint();
                
            }
	    	
	    	 @Override
	    	    public void mouseEntered(MouseEvent e) {
	    	        if (logoInicio.isVisible()) {
	    	            setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	        }
	    	    }

	    	    @Override
	    	    public void mouseExited(MouseEvent e) {
	    	        if (logoInicio.isVisible()) {
	    	            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	    	        }
	    	    }
            
        });
	    
        contentPane.add(logoInicio);
	    
	}

}
