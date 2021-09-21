package app.util;

import app.model.Order;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс выполняет создание заказ-наряда в формате .pdf
 */
public class OrderToPDF {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(OrderToPDF.class);

    public static final String IMG1 = "/home/deniskharlamov/orderToPDF/vwlogo1.jpg";
    public static final String IMG2 = "/home/deniskharlamov/orderToPDF/vwlogo2.jpg";

    public static final String FONT = "/home/deniskharlamov/fonts/TimesNewRomanPSMT.ttf";

    /**
     * Метод создает .pdf файл из объекта класса Order
     * @param order 
     */
    public void manipulatePdf(Order order) {
        List<Cell> listCells = new ArrayList<>();
        String dest = "/home/deniskharlamov/orderToPDF/" + order.getOrderNumber() + ".pdf";
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
            Document doc = new Document(pdfDoc);
            Table table = new Table(new float[]{150, 150, 150, 150, 150});
            PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

            Image img = new Image(ImageDataFactory.create(IMG1));
            Image img1 = new Image(ImageDataFactory.create(IMG2));

            img.setWidth(UnitValue.createPercentValue(10))
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                    .setMarginRight(20).setMarginTop(5);

            img1.setWidth(UnitValue.createPercentValue(25))
                    .setHorizontalAlignment(HorizontalAlignment.LEFT).setMarginTop(5)
                    .setMarginLeft(20);

            listCells.add(new Cell(1, 3).setBorderRight(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER).add(img1));
            listCells.add(new Cell(1, 2).setBorderLeft(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER).add(img));

            listCells.add(new Cell(1, 5).setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER));

            listCells.add(new Cell(1, 5).add("Общество с ограниченной ответственностью \"Гамма Народный\"\n "
                    + "000000, Санкт-Петербург, Петроградская наб, дом № 00, литера А, помещение 00 офис "
                    + "0, +7 (123) 456-78-90").setFont(font).setFontSize(11)
                    .setBorderBottom(Border.NO_BORDER).setBorderTop(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER));

            listCells.add(new Cell(1, 3).add("Получатель\n"
                    + "Общество с ограниченной ответственностью \"Гамма Народный\"")
                    .setFont(font).setFontSize(10).setWidthPercent(60));

            listCells.add(new Cell().add("Сч. №").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER).setWidthPercent(10)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("00000000000000000000").setFont(font)
                    .setFontSize(10).setTextAlignment(TextAlignment.CENTER).setWidthPercent(30)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell(1, 3).add("Банк получателя              ИНН: "
                    + "0000000000 КПП: 000000000").setFont(font).setFontSize(10));

            listCells.add(new Cell().add("БИК").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("000000000").setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setHorizontalAlignment(HorizontalAlignment.LEFT));

            listCells.add(new Cell(1, 3).add("САНКТ-ПЕТЕРБУРГСКИЙ ФИЛИАЛ ПАО \"РОСДОРБАНК\"")
                    .setFont(font).setFontSize(10));

            listCells.add(new Cell().add("Сч. №").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("00000000000000000").setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setHorizontalAlignment(HorizontalAlignment.LEFT));

            listCells.add(new Cell(1, 5).add("Предварительный заказ-наряд "
                    + "№ СНЗН-" + order.getOrderNumber() + " от "
                    + order.getYearMonthDayHour()[2] + " " + order.getMonthCh() + " "
                    + order.getYearMonthDayHour()[0] + " г   "
                    + order.getYearMonthDayHour()[3] + ":00").setFont(font)
                    .setBold().setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("Клиент").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setWidthPercent(15).setBold());

            listCells.add(new Cell().add(order.getClient().getSurname() + " "
                    + order.getClient().getName()).setFont(font)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(10).setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("Телефон").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBold().setWidthPercent(10));

            listCells.add(new Cell(1, 2).add(order.getClient().getTelephone()).setFont(font)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(10).setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("Марка/Модель").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell().add(order.getUserAuto().getBrand() + " / "
                    + order.getUserAuto().getModel()).setFont(font)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT).setMarginLeft(10)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("Гос.ном").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell(1, 2).add(order.getUserAuto().getPlateNumber())
                    .setFont(font).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(10).setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("Двигатель").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell().add(order.getUserAuto().getEngine().getLetterDesignation())
                    .setFont(font).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(10).setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell().add("VIN").setFont(font).setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell(1, 2).add(order.getUserAuto().getVin())
                    .setFont(font).setFontSize(10).setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(10).setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell(1, 5).setBorder(Border.NO_BORDER));

            listCells.add(new Cell(1, 5).add("Плановое техническое обслуживание "
                    + "(" + order.getMileage() + "тыс.км ; " + order.getAge() + " "
                    + (order.getAge() < 5 ? "год(а)" : "лет") + ")").setFont(font).setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            listCells.add(new Cell(1, 5).setBorder(Border.NO_BORDER));

            listCells.add(new Cell(1, 5).add("Перечень работ").setFont(font)
                    .setFontSize(10));

            listCells.add(new Cell(1, 2).add("Наименование работ").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setWidthPercent(50).setBold());

            listCells.add(new Cell().add("Н/Ч").setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell().add("Цена").setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell().add("Стоимость").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBold());

            //---->Attention! Внесение работ и з/ч!<----
            int labor = order.getAmountLabor();
            for (int i = 0; i < labor; i++) {
                listCells.add(new Cell(1, 2).add(order.getLabor(i)
                        .getNameLabor()).setFont(font));

                listCells.add(new Cell().add(String.valueOf(
                        order.getLabor(i).getAmountStandardHour()))
                        .setFont(font).setTextAlignment(TextAlignment.CENTER));

                listCells.add(new Cell().add(String.valueOf(order.getLabor(i)
                        .getCostStandardHour())).setFont(font)
                        .setTextAlignment(TextAlignment.CENTER));

                listCells.add(new Cell().add(
                        String.valueOf(order.getLabor(i).getAmountStandardHour()
                                * order.getLabor(i).getCostStandardHour()))
                        .setFont(font).setTextAlignment(TextAlignment.RIGHT));
            }

            listCells.add(new Cell(1, 3).setBorder(Border.NO_BORDER));

            listCells.add(new Cell().add("Итого").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER));

            listCells.add(new Cell().add(String.valueOf(order.getPriceLabor()))
                    .setFont(font).setTextAlignment(TextAlignment.RIGHT));

            //Шапка строк запчастей!!
            listCells.add(new Cell(1, 5).setBorder(Border.NO_BORDER));

            listCells.add(new Cell(1, 5).add("Запасные части для технического обслуживания")
                    .setFont(font).setFontSize(10));

            listCells.add(new Cell(1, 2).add("Наименование запчасти").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setWidthPercent(50).setBold());

            listCells.add(new Cell().add("Кол-во").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell().add("Цена").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            listCells.add(new Cell().add("Стоимость").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE).setBold());

            //---->Attention! Внесение работ и з/ч!<----
            int parts = order.getAmountPart();
            for (int i = 0; i < parts; i++) {
                listCells.add(new Cell(1, 2).add(order.getPart(i)
                        .getNamePart()).setFont(font));

                listCells.add(new Cell().add(String.valueOf(order.getPart(i).getAmountParts()))
                        .setFont(font).setTextAlignment(TextAlignment.CENTER));

                listCells.add(new Cell().add(String.valueOf(order.getPart(i).getPrice()))
                        .setFont(font).setTextAlignment(TextAlignment.CENTER));

                listCells.add(new Cell().add(String.valueOf(order.getPart(i)
                        .getAmountParts() * order.getPart(i).getPrice()))
                        .setFont(font).setTextAlignment(TextAlignment.RIGHT));

            }

            listCells.add(new Cell(1, 3).setBorder(Border.NO_BORDER));

            listCells.add(new Cell().add("Итого").setFont(font)
                            .setTextAlignment(TextAlignment.CENTER));

            listCells.add(new Cell().add(String.valueOf(order.getPriceOrder()))
                            .setFont(font).setTextAlignment(TextAlignment.RIGHT));

            listCells.add(new Cell(1, 5).setBorder(Border.NO_BORDER));

            //Сумма итоговых работ и материалов
            listCells.add(new Cell(1, 4).add("Работы").setFont(font)
                            .setTextAlignment(TextAlignment.LEFT));

            listCells.add(new Cell().add(String.valueOf(order.getPriceLabor()))
                            .setTextAlignment(TextAlignment.RIGHT));

            listCells.add(new Cell(1, 4).add("Запасные части и расходные материалы")
                            .setFont(font).setTextAlignment(TextAlignment.LEFT));

            listCells.add(new Cell().add(String.valueOf(order.getPriceParts()))
                            .setTextAlignment(TextAlignment.RIGHT));

            listCells.add(new Cell(1, 4).add("Итого").setFont(font)
                            .setTextAlignment(TextAlignment.LEFT).setBold());

            listCells.add(new Cell().add(String.valueOf(order.getPriceOrder()))
                            .setTextAlignment(TextAlignment.RIGHT).setBold());

            for (int i = 0;
                    i < listCells.size();
                    i++) {
                table.addCell(listCells.get(i));
            }

            doc.add(table);
            doc.close();
        } catch (FileNotFoundException ex) {
            logger.error("FileNotFoundException {}", ex);
        } catch (IOException ex) {
            logger.error("IOException {}", ex);
        }
    }
}