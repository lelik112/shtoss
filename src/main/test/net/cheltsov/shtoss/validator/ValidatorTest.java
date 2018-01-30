package net.cheltsov.shtoss.validator;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ValidatorTest {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DATA_FILE_NAME = "data/data.xls";
    private static final String SHEET_EMAIL_VALID = "emailValid";
    private static final String SHEET_EMAIL_INVALID = "emailInvalid";
    private static final String SHEET_LANGUAGE_VALID = "languageValid";
    private static final String SHEET_LANGUAGE_INVALID = "languageInvalid";
    private static final String SHEET_PAYMENT_VALID = "amountValid";
    private static final String SHEET_PAYMENT_INVALID = "amountInvalid";
    private static final String SHEET_PASSWORD_VALID = "passwordValid";
    private static final String SHEET_PASSWORD_INVALID = "passwordInvalid";
    private static final String SHEET_REGISTER_DATA_VALID = "registerDataValid";
    private static final String SHEET_REGISTER_DATA_INVALID = "registerDataInvalid";

    private Workbook workbook;

    @BeforeClass
    public void init() {
        InputStream inputStream = ValidatorTest.class.getClassLoader().getResourceAsStream(DATA_FILE_NAME);
        try {
            workbook = Workbook.getWorkbook(inputStream);
            inputStream.close();
        } catch (IOException | BiffException e) {
            LOGGER.debug(e);
        }
    }

    @AfterClass
    public void destroy() {
        workbook.close();
    }

    @DataProvider(name = "providerValidateRegisterDataTrue")
    public Object[][] providerValidateRegisterDataTrue() {
        return getExcelData(SHEET_REGISTER_DATA_VALID);
    }

    @Test(dataProvider = "providerValidateRegisterDataTrue")
    public void testValidateRegisterDataTrue(String login, String password, String email) {
        assertTrue(Validator.validateRegisterData(login, password, email));
    }

    @DataProvider(name = "providerValidateRegisterDataFalse")
    public Object[][] providerValidateRegisterDataFalse() {
        return getExcelData(SHEET_REGISTER_DATA_INVALID);
    }

    @Test(dataProvider = "providerValidateRegisterDataFalse")
    public void testValidateRegisterDataFalse(String login, String password, String email) {
        assertFalse(Validator.validateRegisterData(login, password, email));
    }

    @DataProvider(name = "providerValidatePaymentTrue")
    public Object[][] providerValidatePaymentTrue() {
        return getExcelData(SHEET_PAYMENT_VALID);
    }

    @Test(dataProvider = "providerValidatePaymentTrue")
    public void testValidatePaymentTrue(String amount) {
        assertTrue(Validator.validatePayment(amount));
    }

    @DataProvider(name = "providerValidatePaymentFalse")
    public Object[][] providerValidatePaymentFalse() {
        return getExcelData(SHEET_PAYMENT_INVALID);
    }

    @Test(dataProvider = "providerValidatePaymentFalse")
    public void testValidatePaymentFalse(String amount) {
        assertFalse(Validator.validatePayment(amount));
    }

    @DataProvider(name = "providerValidateLanguageTrue")
    public Object[][] providerValidateLanguageTrue() {
        return getExcelData(SHEET_LANGUAGE_VALID);
    }

    @Test(dataProvider = "providerValidateLanguageTrue")
    public void testValidateLanguageTrue(String language) {
        assertTrue(Validator.validateLanguage(language));
    }

    @DataProvider(name = "providerValidateLanguageFalse")
    public Object[][] providerValidateLanguageFalse() {
        return getExcelData(SHEET_LANGUAGE_INVALID);
    }

    @Test(dataProvider = "providerValidateLanguageFalse")
    public void testValidateLanguageFalse(String language) {
        assertFalse(Validator.validateLanguage(language));
    }

    @DataProvider(name = "providerValidatePasswordTrue")
    public Object[][] providerValidatePasswordTrue() {
        return getExcelData(SHEET_PASSWORD_VALID);
    }

    @Test(dataProvider = "providerValidatePasswordTrue")
    public void testValidatePasswordTrue(String password) {
        assertTrue(Validator.validatePassword(password));
    }

    @DataProvider(name = "providerValidatePasswordFalse")
    public Object[][] providerPasswordFalse() {
        return getExcelData(SHEET_PASSWORD_INVALID);
    }

    @Test(dataProvider = "providerValidatePasswordFalse")
    public void testValidatePasswordFalse(String password) {
        assertFalse(Validator.validatePassword(password));
    }

    @DataProvider(name = "providerValidateEmailTrue")
    public Object[][] providerValidateEmailTrue() {
        return getExcelData(SHEET_EMAIL_VALID);
    }

    @Test(dataProvider = "providerValidateEmailTrue")
    public void testValidateEmailTrue(String email) {
        assertTrue(Validator.validateEmail(email));
    }

    @DataProvider(name = "providerValidateEmailFalse")
    public Object[][] provider() {
        return getExcelData(SHEET_EMAIL_INVALID);
    }

    @Test(dataProvider = "providerValidateEmailFalse")
    public void testValidateEmailFalse(String email) {
        assertFalse(Validator.validateEmail(email));
    }

    private String[][] getExcelData(String sheetName) {
        Sheet sh = workbook.getSheet(sheetName);
        String[][] arrayExcelData = new String[sh.getRows()][sh.getColumns()];
        for (int i = 0; i < sh.getRows(); i++) {
            for (int j = 0; j < sh.getColumns(); j++) {
                arrayExcelData[i][j] = sh.getCell(j, i).getContents();
            }
        }
        return arrayExcelData;
    }
}