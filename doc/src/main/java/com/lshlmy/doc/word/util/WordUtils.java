package com.lshlmy.doc.word.util;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import sun.management.counter.Units;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.math.BigInteger;

/**
 * Description: @TODO
 * User: lshlmy
 * Date: 2017/12/9 19:36
 */
public final class WordUtils {

    private WordUtils() {
    }

    /**
     * 添加标题样式
     * @param docxDocument 文档
     * @param strStyleId 样式id
     * @param headingLevel 标题级别
     */
    public static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);


        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        //ppr.setOutlineLvl(indentNumber);

        CTNumPr ctNumPr = CTNumPr.Factory.newInstance();

        //
        CTDecimalNumber ctDecimalNumber = CTDecimalNumber.Factory.newInstance();
        ctDecimalNumber.setVal(BigInteger.valueOf(1));
        ctNumPr.setNumId(ctDecimalNumber);
        ppr.setNumPr(ctNumPr);

        CTOnOff onoffnull1 = CTOnOff.Factory.newInstance();
        onoffnull1.setVal(STOnOff.X_0);
        ppr.setWidowControl(onoffnull1);
        CTJc ctJc = CTJc.Factory.newInstance();
        ctJc.setVal(STJc.BOTH);
        ppr.setJc(ctJc);

        ctStyle.setPPr(ppr);

        /*CTString ctString = CTString.Factory.newInstance();
        ctString.setVal("a");
        ctStyle.setBasedOn(ctString);

        ctString.setVal("1230");
        ctStyle.setLink(ctString);*/


        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        System.out.println(ctStyle);
        styles.addStyle(style);

    }


    public static void addPicture(XWPFParagraph paragraph, XWPFDocument document, String base64Picture) {
        byte[] bytes = Base64.decodeBase64(base64Picture.getBytes());
        File file = FileUtils.getFile("C:/Users/lshlmy/Desktop/新建文件夹/test1.png");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            String blipId = document.addPictureData(inputStream, Document.PICTURE_TYPE_PNG);
            //createPicture(blipId, document.getNextPicNameNumber(Document.PICTURE_TYPE_PNG), 200, 200, paragraph,document);

            XWPFRun r = paragraph.createRun();

            r.addBreak();
            XWPFPicture xwpfPicture = r.addPicture(new FileInputStream("C:\\Users\\lshlmy\\Desktop\\timg.jpg"), Document.PICTURE_TYPE_JPEG, "dasdasd", Units.toUnits(200).intValue(), Units.toUnits(200).intValue()); // 200x200 pixels
            xwpfPicture.getCTPicture().getBlipFill().addNewBlip().
            r.addBreak(BreakType.PAGE);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createPicture(String blipId, int id, int width, int height,
                              XWPFParagraph paragraph,XWPFDocument document) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        // String blipId =
        // getAllPictures().get(id).getPackageRelationship().getId();

        CTInline inline = paragraph.createRun().getCTR().addNewDrawing()
                .addNewInline();
        String picXml = ""
                + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
                + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
                + id
                + "\" name=\"img_"
                + id
                + "\"/>"
                + "            <pic:cNvPicPr/>"
                + "         </pic:nvPicPr>"
                + "         <pic:blipFill>"
                + "            <a:blip r:embed=\""
                + blipId
                + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
                + "            <a:stretch>"
                + "               <a:fillRect/>"
                + "            </a:stretch>"
                + "         </pic:blipFill>"
                + "         <pic:spPr>"
                + "            <a:xfrm>"
                + "               <a:off x=\"0\" y=\"0\"/>"
                + "               <a:ext cx=\""
                + width
                + "\" cy=\""
                + height
                + "\"/>"
                + "            </a:xfrm>"
                + "            <a:prstGeom prst=\"rect\">"
                + "               <a:avLst/>"
                + "            </a:prstGeom>"
                + "         </pic:spPr>"
                + "      </pic:pic>"
                + "   </a:graphicData>" + "</a:graphic>";
        // CTGraphicalObjectData graphicData =
        // inline.addNewGraphic().addNewGraphicData();
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        // graphicData.set(xmlToken);
        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);


        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("docx_img_ " + id);

        docPr.setDescr("docx Picture");


    }
}
