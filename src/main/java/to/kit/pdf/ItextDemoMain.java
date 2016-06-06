package to.kit.pdf;

import java.io.IOException;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;

import to.kit.pdf.wrap.PDF;

public class ItextDemoMain {
	private static final String STR = "01234567890, Alphabet, ひらがな, カタカナ, ｶﾀｶﾅ, 漢字, 髙﨑";

	public void execute() {
		try {
			PDF pdf = new PDF("タイトルにゃんこ", "にゃんこ髙﨑", "UseIText.pdf");

			float h = pdf.getPageSize().getHeight();
			float w = pdf.getPageSize().getWidth();
			float c = w / 2;
			PdfContentByte content = pdf.getDirectContent();

			pdf.setFont("HeiseiKakuGo-W5", 20);
			content.setTextMatrix(1f, 0f, 0.3f, 1f, 200, h - 50);
			pdf.drawString("【髙】はしごだか");
			pdf.newLine();
			pdf.drawString("【﨑】たつさき");

			pdf.drawLine(30, 140, 200, 140);
			pdf.drawOval(312, 297, 28, 10);

			pdf.setFontColor(255, 0, 0);
			pdf.setFont("HeiseiMin-W3", 12);
			pdf.drawString("HeiseiMin-W3", 20, 20);
			pdf.drawString(STR, 20, 35);

			pdf.setFontColor(0, 255, 0);
			pdf.setFont("HeiseiKakuGo-W5", 12);
			pdf.drawString("HeiseiKakuGo-W5", 20, 60);
			pdf.drawString(STR, 20, 75);

			pdf.setFontColor(0, 0, 255);
			pdf.setFont("HeiseiMin-W3", 12);
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, "HeiseiMin-W3", c, h - 100, 0);
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, STR, c, h - 115, 0);

			pdf.setFontColor(0, 0, 0);
			pdf.setFont("HeiseiKakuGo-W5", 12);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, "HeiseiKakuGo-W5", w - 20, h - 140, 0);
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, STR, w - 20, h - 155, 0);

			Image image = pdf.createImage("name", "/nekokoclub.jpeg");
			pdf.drawImage(image, 20, 200, 192, 150);

			pdf.addNewPage("A4L");
			pdf.setFont("HeiseiKakuGo-W5", 20);
			pdf.drawString(STR, 5, 50);

			pdf.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ItextDemoMain app = new ItextDemoMain();

		app.execute();
	}
}
