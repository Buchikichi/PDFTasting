package to.kit.pdf.wrap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * PdfWriterのラッパー.
 * @author Hidetaka Sasai
 */
public class PDF extends PdfWriter {
	private Document doc = new Document();
	private Map<String, BaseFont> fontMap = new HashMap<>();
	private boolean isTextBegan = false;

	private BaseFont getFont(String fontName) {
		BaseFont font;
		if (this.fontMap.containsKey(fontName)) {
			font = this.fontMap.get(fontName);
		} else {
			try {
				font = BaseFont.createFont(fontName, "UniJIS-UTF8-H", BaseFont.NOT_EMBEDDED);
			} catch (DocumentException | IOException e) {
				e.printStackTrace();
				font = null;
			}
		}
		return font;
	}

	private void beginText() {
		if (!this.isTextBegan) {
			PdfContentByte content = getDirectContent();

			content.beginText();
			this.isTextBegan = true;
		}
	}

	private void endText() {
		if (this.isTextBegan) {
			PdfContentByte content = getDirectContent();

			content.endText();
			this.isTextBegan = false;
		}
	}

	public void addNewPage() {
		endText();
		this.doc.newPage();
	}

	/**
	 * ページを追加.
	 * @param width 幅
	 * @param height 高さ
	 */
	public void addNewPage(float width, float height) {
		this.doc.setPageSize(new Rectangle(width, height));
		addNewPage();
	}

	/**
	 * ページを追加.
	 * @param size サイズ名
	 */
	public void addNewPage(String size) {
		if ("A4L".equals(size)) {
			this.doc.setPageSize(PageSize.A4.rotate());
		} else {
			this.doc.setPageSize(PageSize.getRectangle(size));
		}
		addNewPage();
	}

	/**
	 * 画像イメージオブジェクトを生成.
	 * @param imageName イメージ名
	 * @param fileName ファイル名
	 * @return 画像イメージオブジェクト
	 */
	public Image createImage(final String imageName, final String fileName) {
		Image image = null;
		URL url = PDF.class.getResource(fileName);

		try {
			if (url != null) {
				image = Image.getInstance(url);
			} else {
				image = Image.getInstance(fileName);
			}
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 画像を描画.
	 * @param image 画像イメージ
	 * @param x X座標
	 * @param y Y座標
	 * @param width 幅
	 * @param height 高さ
	 */
	public void drawImage(final Image image, final float x, final float y, final float width, final float height) {
		float h = getPageSize().getHeight();
		PdfContentByte content = getDirectContent();

		endText();
		try {
			content.addImage(image, width, 0, 0, height, x, h - y - height);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 線を描画.
	 * @param x1 X1座標
	 * @param y1 Y1座標
	 * @param x2 X2座標
	 * @param y2 Y2座標
	 */
	public void drawLine(float x1, float y1, float x2, float y2) {
		float h = getPageSize().getHeight();
		PdfContentByte content = getDirectContent();

		endText();
		content.moveTo(x1, h - y1);
		content.lineTo(x2, h - y2);
		content.stroke();
	}

	/**
	 * 楕円を描画.
	 * @param x X座標
	 * @param y Y座標
	 * @param width 幅
	 * @param height 高さ
	 */
	public void drawOval(final float x, final float y, final float width, final float height) {
		float h = getPageSize().getHeight();
		float y1 = h - y;
		float x2 = x + width;
		float y2 = y1 - height;
		PdfContentByte content = getDirectContent();

		endText();
		content.ellipse(x, y1, x2, y2);
		content.stroke();
	}

	/**
	 * 文字列を描画.
	 * @param str 文字列
	 */
	public void drawString(final String str) {
		PdfContentByte content = getDirectContent();

		beginText();
		content.showText(str);
	}

	/**
	 * 文字列を描画.
	 * @param str 文字列
	 * @param x X座標
	 * @param y Y座標
	 */
	public void drawString(final String str, final float x, final float y) {
		float h = getPageSize().getHeight();
		PdfContentByte content = getDirectContent();

		content.setTextMatrix(x, h - y);
		drawString(str);
	}

	/**
	 * 楕円を塗りつぶし.
	 * @param x X座標
	 * @param y Y座標
	 * @param width 幅
	 * @param height 高さ
	 */
	public void fillOval(final float x, final float y, final float width, final float height) {
		float h = getPageSize().getHeight();
		float y1 = h - y;
		float x2 = x + width;
		float y2 = y1 - height;
		PdfContentByte content = getDirectContent();

		endText();
		content.ellipse(x, y1, x2, y2);
		content.fill();
	}

	/**
	 * 改行.
	 * @param leading 改行幅
	 */
	public void newLine(final float leading) {
		PdfContentByte content = getDirectContent();

		content.setLeading(leading);
		content.newlineText();
	}

	/**
	 * 改行.
	 */
	public void newLine() {
		PdfContentByte content = getDirectContent();

		content.newlineText();
	}

	/**
	 * 描画色を設定.
	 * @param r 赤
	 * @param g 緑
	 * @param b 青
	 */
	public void setColor(final int r, final int g, final int b) {
		PdfContentByte content = getDirectContent();

		content.setRGBColorStroke(r, g, b);
	}

	/**
	 * 描画色を設定.
	 * @param colorName
	 */
	public void setColor(final String colorName) {
		// ※ "black" しか対応していません!!!
		setColor(0, 0, 0);
	}

	/**
	 * フォントとサイズを設定.
	 * @param fontName フォント名
	 * @param size サイズ
	 */
	public void setFont(final String fontName, final float size) {
		BaseFont font = getFont(fontName);

		if (font != null) {
			PdfContentByte content = getDirectContent();

			beginText();
			content.setFontAndSize(font, size);
			content.setLeading(size);
		}
	}

	/**
	 * フォントの色を設定.
	 * @param r 赤
	 * @param g 緑
	 * @param b 青
	 */
	public void setFontColor(final int r, final int g, final int b) {
		PdfContentByte content = getDirectContent();

		content.setRGBColorFill(r, g, b);
	}

	/**
	 * フォントの色を設定.
	 * @param colorName フォントの色名
	 */
	public void setFontColor(final String colorName) {
		// ※ "black" しか対応していません!!!
		setFontColor(0, 0, 0);
	}

	/**
	 * 線の幅を設定.
	 * @param width 線の幅
	 */
	public void setLineWidth(final float width) {
		PdfContentByte content = getDirectContent();

		content.setLineWidth(width);
	}

	/**
	 * ドキュメントを保存.
	 */
	public void closeDocument() {
		endText();
		this.doc.close();
	}

	/**
	 * 【互換】ドキュメントを保存.
	 */
	public void writeFile() {
		closeDocument();
	}

	/**
	 * インスタンス生成.
	 * @param title
	 * @param author
	 * @param out
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public PDF(final String title, final String author, final String filename) throws IOException {
		super(new WrappedPdfDocument(), new FileOutputStream(filename));
		this.doc.addDocListener(super.pdf);
		try {
			super.pdf.addWriter(this);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
		this.doc.addTitle(title);
		this.doc.addAuthor(author);
		this.doc.open();
	}
}
