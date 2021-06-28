package hufs.cse.grimpan1;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;

public class GrimPan1 extends JFrame {

	private GrimPanModel model = null;
	
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuHelp;
	private JMenuItem jmiNew;
	private JMenuItem jmiOpen;
	private JMenuItem jmiExit;
	private JMenuItem jmiAbout;

	GrimPan1 thisClass =  this;
	private DrawPanel drawPanel;
	private JMenu menuShape;
	private JMenu menuSetting;
	JRadioButtonMenuItem rdbtnmntmLine;
	JRadioButtonMenuItem rdbtnmntmPen;	
	JRadioButtonMenuItem rdbtnmntmPoly;

	private JMenuItem jmiStrokeWidth;
	private JMenuItem jmiStrokeColor;
	private JMenuItem jmiFillColor;
	private JCheckBoxMenuItem jcmiFill;

	private ButtonGroup btnGroup = new ButtonGroup();

	/**
	 * Create the frame.
	 */
	public GrimPan1() {
		model = new GrimPanModel();
		initialize();
	}
	
	void initialize(){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 50, 800, 600);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuFile = new JMenu("File  ");
		menuBar.add(menuFile);
		
		jmiNew = new JMenuItem("New  ");
		menuFile.add(jmiNew);
		jmiNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawPanel.setVisible(false);
				contentPane.remove(drawPanel);
				model.shapeList.clear();  // shapeList�� clear�ϰ� ������ ���� �����ϰ� �Ⱥ��̰� ��
				
				model = new GrimPanModel();
				drawPanel = new DrawPanel(model);
				contentPane.add(drawPanel);
				drawPanel.setVisible(true);  // ���ο� ���� �߰��ϰ� ���̰� ��
				
				rdbtnmntmLine.setSelected(true);
				rdbtnmntmPen.setSelected(false);
				rdbtnmntmPoly.setSelected(false);
				jcmiFill.setSelected(false);  // �����ڽ�, üũ�ڽ��� ��� �ʱ� ���·� ������	
			}
		});
		
		jmiOpen = new JMenuItem("Open ");
		menuFile.add(jmiOpen);
		
		menuFile.addSeparator();
		
		jmiExit = new JMenuItem("Exit  ");
		jmiExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuFile.add(jmiExit);
		
		menuShape = new JMenu("Shape  ");
		menuBar.add(menuShape);
		
		rdbtnmntmLine = new JRadioButtonMenuItem("����");
		rdbtnmntmLine.setSelected(true);
		rdbtnmntmLine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				model.setEditState(GrimPanModel.SHAPE_LINE);
			}
		});
		menuShape.add(rdbtnmntmLine);
		
		rdbtnmntmPen = new JRadioButtonMenuItem("����");
		rdbtnmntmPen.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				model.setEditState(GrimPanModel.SHAPE_PENCIL);

			}
		});
		menuShape.add(rdbtnmntmPen);
		
		rdbtnmntmPoly = new JRadioButtonMenuItem("�ٰ���");
		rdbtnmntmPoly.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				model.setEditState(GrimPanModel.SHAPE_POLYGON);
			}
		});
		menuShape.add(rdbtnmntmPoly);
		
		btnGroup.add(rdbtnmntmLine);
		btnGroup.add(rdbtnmntmPen);
		btnGroup.add(rdbtnmntmPoly);
		
		menuSetting = new JMenu("Setting ");
		menuBar.add(menuSetting);
		
		jmiStrokeWidth = new JMenuItem("\uC120\uB450\uAED8");
		jmiStrokeWidth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputVal = JOptionPane.showInputDialog("line thickness", "1");  // �� �β� ���� â�� ���
				if (inputVal!=null){
					model.setShapeStroke(new BasicStroke(Integer.parseInt(inputVal)));
					drawPanel.repaint();  // ������ �� �β��� ������
				}
			}
		});
		menuSetting.add(jmiStrokeWidth);
		
		jmiStrokeColor = new JMenuItem("\uC120\uC0C9\uAE54");
		jmiStrokeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(thisClass, "Color Choosing", Color.black);  // ������ ���� â�� ���				
					model.setStrokeColor(color);
					jcmiFill.setSelected(false);
					drawPanel.repaint();  // ������ ������ �����ϰ� 'ä���'�� üũ�� ������ (ä��� ����)
			}
		});
		menuSetting.add(jmiStrokeColor);
		
		jcmiFill = new JCheckBoxMenuItem("\uCC44\uC6B0\uAE30");
		jcmiFill.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean fillState = jcmiFill.getState();
				model.setShapeFill(fillState);
				drawPanel.repaint();
			}
		});
		menuSetting.add(jcmiFill);
		
		jmiFillColor = new JMenuItem("\uCC44\uC6C0\uC0C9\uAE54");
		jmiFillColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(thisClass, "Color Choosing", Color.black);  // ä����� ���� â�� ���					
				model.setFillColor(color);
				jcmiFill.setSelected(true);
				drawPanel.repaint();  // ������ ������ �����ϰ� 'ä���'�� üũ�� (ä��� ����)
			}
		});
		menuSetting.add(jmiFillColor);
		
		menuHelp = new JMenu("Help  ");
		menuBar.add(menuHelp);
		
		jmiAbout = new JMenuItem("About");
		jmiAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(thisClass,
						"GrimPan Ver0.3 \nProgrammed by cskim, cse, hufs.ac.kr", 
						 "About", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		menuHelp.add(jmiAbout);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		drawPanel = new DrawPanel(model);
		contentPane.add(drawPanel, BorderLayout.CENTER);
	}

}
