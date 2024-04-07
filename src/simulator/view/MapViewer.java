package simulator.view;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.MapInfo;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewer extends AbstractMapViewer{

	private int _width;
	private int _height;

	private int _rows;
	private int _cols;

	int _rwidth;
	int _rheight;

	private Animal _selectedAnimal;

	Animal.State _currState;

	volatile private Collection<AnimalInfo> _objs;
	volatile private Double _time;

	private static class SpeciesInfo {
		private Integer _count;
		private Color _color;

		SpeciesInfo(Color color) {
			_count = 0;
			_color = color;
		}
	}

	Map<String, SpeciesInfo> _kindsInfo = new HashMap<>();

	private Font _font = new Font("Arial", Font.BOLD, 12);

	private boolean _showHelp;

	public MapViewer(Controller _ctrl) {
		initGUI();
	}

	private void initGUI() {

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
					case 'h':
						_showHelp = !_showHelp;
						repaint();
						break;
					case 's':
						repaint();
					default:
				}
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}
		});

		_currState = null;
		_showHelp = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setFont(_font);

		gr.setBackground(Color.WHITE);
		gr.clearRect(0, 0, _width, _height);

		if (_objs != null)
			drawObjects(gr, _objs, _time);
	}

	private boolean visible(AnimalInfo a) {
		return visible(a);
	}

	private void drawObjects(Graphics2D g, Collection<AnimalInfo> animals, Double time) {

		// Draw the grid of regions

		// Draw the animals
		for (AnimalInfo a : animals) {

			// If not visible, skip iteration
			if (!visible(a))
				continue;

			// Species information of 'a'
			SpeciesInfo esp_info = _kindsInfo.get(a.get_genetic_code());

			// Add an entry to the map if esp_info is null
			// Use ViewUtils.get_color(a.get_genetic_code()) for color

			// Increment the species counter
		}

		// Draw the visible state label, if not null

		// Draw the time label
		// Use String.format("%.3f", time) to write only 3 decimals

		// Draw the information of all species
		// At the end of each iteration, set the species counter to 0 to reset it
	}

	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {
		// Store objs and time in the corresponding attributes and call repaint() to redraw the component
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {
		// Update the attributes _width, _height, _cols, _rows, etc.

		// This changes the size of the component, thus changing the size of the window because in MapWindow we call pack() after calling reset
		setPreferredSize(new Dimension(map.get_width(), map.get_height()));

		// Draw the state
		update(animals, time);
	}

}
