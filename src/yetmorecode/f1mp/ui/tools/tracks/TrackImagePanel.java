package yetmorecode.f1mp.ui.tools.tracks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.track.Track;
import yetmorecode.f1mp.model.track.TrackSegment;

interface GetTrackDataValue {
	float get(TrackSegment seg);
}

public class TrackImagePanel extends JPanel {

	private static final long serialVersionUID = -6281209087957990326L;
	
	private Track track;
	private GetTrackDataValue getValue;
	
	public float zMax;
	public float zMin;
	
	public TrackImagePanel(Track track, GetTrackDataValue f) {
		this.track = track;
		this.getValue = f;
		
		zMax = Float.MIN_VALUE;
	    zMin = Float.MAX_VALUE;
		for (int i=0; i < track.segments.size(); i++) {
	    	var seg = track.segments.get(i);
	    	zMax = Math.max(zMax, f.get(seg));
	    	zMin = Math.min(zMin, f.get(seg));
	    }
		
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    
	    //paintLine(graphics, Color.GREEN, f);
	    paintTrack(graphics, Color.YELLOW, Color.RED);
		
		setPreferredSize(new Dimension(Integer.MAX_VALUE, 400));
	}
	
	private void paintTrack(Graphics graphics, Color color1, Color color2) {
		Graphics2D g2d = (Graphics2D) graphics;
		Dimension size = getSize();
	    var width = size.width;
	    var height = size.height;
	    var xMax = 0;
	    var xMin = 0;
	    var yMax = 0;
	    var yMin = 0;
		for (int i=0; i < track.segments.size(); i++) {
	    	var seg = track.segments.get(i);
	    	xMax = Math.max(xMax, seg.x);
	    	xMin = Math.min(xMin, seg.x);
	    	yMax = Math.max(yMax, seg.y);
	    	yMin = Math.min(yMin, seg.y);
	    }
		
		var trackHeight = yMax - yMin;
		var trackWidth = xMax - xMin;
		var ratio = 1.0 * trackHeight / trackWidth;
		if (trackHeight > trackWidth) {
			// crop width to keep aspect ratio
			width = (int) (width / ratio);
		} else {
			// crop height to keep aspect ratio
			height = (int) (height * ratio);
		}
		
		// Draw track
		for (int i=0; i < track.segments.size(); i++) {
	    	var seg = track.segments.get(i);
	    	//var screenx = (1.0 * i / total) * width;
	    	var screenx = width - (((1.0 * seg.x - xMin) / (xMax - xMin))) * width;
	    	var screeny = (((1.0 * seg.y - yMin) / (yMax - yMin))) * height;
	    	
	    	var colorPercent = 1.0 * (zMax != zMin ? (1.0 * getValue.get(seg) - zMin) / (zMax - zMin) : 1);
	    	var c = new Color(
	    		(int)(color1.getRed() + colorPercent * (color2.getRed() - color1.getRed())),
	    		(int)(color1.getGreen() + colorPercent * (color2.getGreen() - color1.getGreen())),
	    		(int)(color1.getBlue() + colorPercent * (color2.getBlue() - color1.getBlue())),
	    		130
	    	);
	    	
	    	int maxRectHeight = 30;
	    	int rectHeight = (int) (colorPercent * maxRectHeight + 1);
	    	g2d.setColor(c);
    		g2d.fillRect((int)screenx, ((int)screeny - rectHeight + maxRectHeight + 5), 1, (int) rectHeight);
    		g2d.setColor(Color.LIGHT_GRAY);
    		g2d.fillRect((int)screenx, ((int)screeny - rectHeight + maxRectHeight  + 5), 1, 1);
    		if (i == track.pitEntry || i == track.pitExit) {
    			var extra = 20;
    			g2d.fillRect((int)screenx, ((int)screeny - rectHeight - (extra/2) + maxRectHeight + 5), 2, (int) rectHeight + extra);		
    		}
    		
    		if (i == 0) {
    			var extra = 20;
    			g2d.setColor(Color.WHITE);
    			g2d.fillRect((int)screenx, ((int)screeny - rectHeight - (extra/2) + maxRectHeight + 5), 2, (int) rectHeight + extra);		
    		}
		}
		
		// Draw pitlane
		for (int i=0; i < track.points.size(); i++) {
	    	var seg = track.points.get(i);
	    	var screenx = width - (((1.0 * seg.x - xMin) / (xMax - xMin))) * width;
	    	var screeny = (((1.0 * seg.y - yMin) / (yMax - yMin))) * height;
	    	int maxRectHeight = 30;
	    	if (i >= track.pitLimiterEntry && i <= track.pitLimiterExit) {
	    		g2d.setColor(Color.GREEN);
	    	} else {
	    		g2d.setColor(Color.PINK);	
	    	}
    		g2d.fillRect((int)screenx, ((int)screeny + maxRectHeight + 5 - 1), 1, 1);
    		
		}
		
		// Draw garages on pitlane
		for (int i = 0; i < track.garages.size(); i++) {
			var pos = track.garages.get(i);
			var seg = track.points.get(pos);
	    	var screenx = width - (((1.0 * seg.x - xMin) / (xMax - xMin))) * width;
	    	var screeny = (((1.0 * seg.y - yMin) / (yMax - yMin))) * height;
	    	int maxRectHeight = 30;
	    	g2d.setColor(Color.CYAN);	
    		g2d.fillRect((int)screenx, ((int)screeny + maxRectHeight + 5 - 5), 1, 10);
		}
		
		// Draw cameras
		for (int i = 0; i < 30; i++) {
			var cam = track.cameras.get(i);
			if (cam.position != -1) {
				var seg = track.segments.get((cam.position + track.segmentsTotal) % track.segmentsTotal);
		    	var screenx = width - (((1.0 * seg.x - xMin) / (xMax - xMin))) * width;
		    	var screeny = (((1.0 * seg.y - yMin) / (yMax - yMin))) * height;
		    	int maxRectHeight = 30;
		    	g2d.setColor(Color.ORANGE);	
	    		g2d.fillRect((int)screenx, ((int)screeny + maxRectHeight + 5 - 5), 1, 10);	
			}
			
			
		}
	}
		
	
	protected void paintLine(Graphics graphics, Color color, GetTrackDataValue f) {
		Graphics2D g2d = (Graphics2D) graphics;
		Dimension size = getSize();
	    var width = size.width;
	    var height = size.height;	    
	    var total = track.segmentsTotal;
		for (int i=0; i < track.segments.size(); i++) {
	    	var seg = track.segments.get(i);
	    	var screenx = (1.0 * i / total) * width;
	    	var screeny = ((zMin != zMax) ? (((1.0 * f.get(seg) - zMin) / (zMax - zMin))) * height : 0);
	    	g2d.setColor(color);
    		g2d.fillRect((int)screenx, (int)screeny, 1, 1);	
		}
	}

}
