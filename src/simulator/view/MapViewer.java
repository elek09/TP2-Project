package simulator.view;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.MapInfo;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;

public class MapViewer extends AbstractMapViewer{

	private int _width;
	private int _height;

	private int _rows;
	private int _cols;

	int _region_width;
	int _region_height;

	private Animal _selectedAnimal;

	Animal.State _currState;

	volatile private Collection<AnimalInfo> _objs;
	volatile private Double _time;

	private Timer timer;

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

		// TODO Draw the grid of regions
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(j * _region_width, i * _region_height, _region_width, _region_height);
			}
		}

		// Draw the animals
		for (AnimalInfo a : animals) {


			// If not visible, skip iteration
			if (!visible(a))
				continue;

			// Species information of 'a'
			SpeciesInfo esp_info = _kindsInfo.computeIfAbsent(a.get_genetic_code(), k -> new SpeciesInfo(ViewUtils.get_color(a.get_genetic_code())));

			// Add an entry to the map if esp_info is null
			if (esp_info == null) {
				esp_info = new SpeciesInfo(ViewUtils.get_color(a.get_genetic_code()));
				_kindsInfo.put(a.get_genetic_code(), esp_info);
			}
			// Use ViewUtils.get_color(a.get_genetic_code()) for color

			// Increment the species counter
			esp_info._count++;
		}

		// Draw the visible state label, if not null
		if (_currState != null) {
			g.setColor(Color.BLACK);
			drawStringWithRect(g, 10, 20, "State: " + _currState);
		}

		// Draw the time label
		// Use String.format("%.3f", time) to write only 3 decimals
		if (time != null) {
		g.setColor(Color.BLACK);
		drawStringWithRect(g, 10, 40, "Time: " + String.format("%.3f", time));
		}


		// Draw the information of all species
		// At the end of each iteration, set the species counter to 0 to reset it
		int i = 0;
		for (Map.Entry<String, SpeciesInfo> entry : _kindsInfo.entrySet()) {
			SpeciesInfo info = entry.getValue();
			g.setColor(info._color);
			drawStringWithRect(g, 10, 60 + i * 20, entry.getKey() + ": " + info._count);
			info._count = 0;
			i++;
		}
	}

	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {
		//TODO Store objs and time in the corresponding attributes and call repaint() to redraw the component
		_objs = objs;
		_time = time;
		repaint();
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Update the attributes _width, _height, _cols, _rows, etc.
		this._cols = map.get_cols();
		this._rows = map.get_rows();
		this._width = map.get_width();
		this._height = map.get_height();
		this._region_width = _width / _cols;
		this._region_height = _height / _rows;

		// TODO This changes the size of the component, thus changing the size of the window because in MapWindow we call pack() after calling reset
		setPreferredSize(new Dimension(map.get_width(), map.get_height()));

		// TODO Draw the state
		update(animals, time);
	}

}
