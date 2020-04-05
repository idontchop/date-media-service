package com.idontchop.datemediaservice.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idontchop.datemediaservice.dtos.Crop;

@Service
public class ImageService {
	
	@Value ("${date.media.max-width}")
	private int MAXWIDTH;
	
	@Value ("${date.media.max-height}")
	private int MAXHEIGHT;


	/**
	 * Takes a byte array and crops it based on the Crop Dto.
	 * 
	 * Overloaded to accept file, bufferedimage.
	 * 
	 * @param image
	 * @param crop
	 * @return
	 * @throws IOException If not image
	 */
	public byte[] cropImage ( byte[] image, Crop crop ) throws IOException {
		
		// coverts byte array to buffered image 
		InputStream in;
		in = new ByteArrayInputStream(image);			
		BufferedImage bufferedImage = ImageIO.read(in);
		
		// process buffered image
		bufferedImage = cropImage ( bufferedImage, crop );
		
		// convert back to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if ( !ImageIO.write(bufferedImage, "jpg", baos) ) {
			// This would occur if library couldn't write the jpeg but didn't have IO problem
			// unlikely to happen
			throw new IOException ("IO.write returned false");
		}
		return baos.toByteArray();
		
	}
	
	/**
	 * This doesn't change the type of image. If you send it a png, it will output a png.
	 * 
	 * Use overloaded (byte[] image, crop) to convert all images to jpg
	 * 
	 * @param bufferedImage
	 * @param crop
	 * @return
	 * @throws IOException
	 */
	public BufferedImage cropImage ( BufferedImage bufferedImage, Crop crop ) throws IOException {
		
		// saved width and height to easytype variable
		// crop values should be passed by %
		int w = bufferedImage.getWidth();
		int h = bufferedImage.getHeight();
		
		// if crop passed by pixel, we can use units directly
		if (crop.getUnit().equals("px") ) {
			bufferedImage = bufferedImage.getSubimage( (int) crop.getX(), (int) crop.getY(), (int) crop.getWidth(), (int) crop.getHeight());
		} else { // crop.getUnit().equals("%") if more are added (unlikely)
			// by percentage, will need to convert
			// this is caused by container resizing
			// plan: write crop method that produces proper numbers from image width
			bufferedImage = bufferedImage.getSubimage( lfp(crop.getX(),w), lfp(crop.getY(), h), lfp(crop.getWidth(), w), lfp(crop.getHeight(), h));
		}
		
		// Required to convert some pngs
		BufferedImage result = new BufferedImage ( bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		result.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null); // changes transparent to white
		
		// resize if width greater than MAXWIDTH
		if (result.getWidth() > MAXWIDTH) {
			return resize (result);
		} else return result;
	}
	
	public byte[] cropImage ( MultipartFile file, Crop crop ) throws IOException {

			return cropImage ( file.getBytes(), crop );

	}
	
	/**
	 * TODO: check for really tall images.
	 * 
	 * For now, we resize to MAXWIDTH keeping the aspect ratio.
	 * 
	 * @param bufferedImage
	 * @return
	 * @throws IOException
	 */
	public BufferedImage resize ( BufferedImage bufferedImage ) throws IOException {
		
		// https://stackoverflow.com/questions/9417356/bufferedimage-resize
		double resizeRatio =  bufferedImage.getWidth() / MAXWIDTH;
		int newWidth = (int) (bufferedImage.getWidth() / resizeRatio);
		int newHeight = (int) (bufferedImage.getHeight() / resizeRatio);
		
		Image tmp = bufferedImage.getScaledInstance( newWidth, newHeight , Image.SCALE_SMOOTH);
		
		BufferedImage newBufferedImage = new BufferedImage( newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = newBufferedImage.createGraphics();
		g2d.drawImage( tmp, 0, 0, null);
		g2d.dispose();
		
		return newBufferedImage;
	}
	
	/**
	 * width from percent
	 * 
	 * converts a double length percentage from a supplied total length to int
	 * @return
	 */
	private int lfp( double percentage, int length) {
		return (int) ( percentage * (double) length)/100;
	}
}
