package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		//Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
		Assertions.assertTrue(driver.getPageSource().contains("Login"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RTTT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertTrue(driver.getPageSource().contains("Login"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UTTT","123");
		doLogIn("UTTT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertTrue(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testCantAccessHome (){
		String host = "http://localhost:" + this.port;

		doMockSignUp("URL","Test","USER1","123");
		doLogIn("USER1", "123");

		Assertions.assertTrue(driver.getCurrentUrl().endsWith("/home"));

		WebElement buttonLogout = driver.findElement(By.id("btn-logout"));
		buttonLogout.click();

		Assertions.assertEquals(host +"/login?message=logout",driver.getCurrentUrl());

		//check access to home with no user
		driver.get(host);
		Assertions.assertTrue(driver.getCurrentUrl().endsWith("/login"));
	}

	private void createNote (String username, String password){
		doMockSignUp("Large File","Test",username,password);
		doLogIn(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab Notes
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note")));
		WebElement btnAddNote = driver.findElement(By.id("add-note"));
		btnAddNote.click();

		//fill input
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.sendKeys("test create note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.sendKeys("note description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit-modal")));
		WebElement noteSubmit = driver.findElement(By.id("note-submit-modal"));
		noteSubmit.click();
	}

	@Test
	public void testCreateNote () {
		createNote("CREATENOTE", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//check create successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-message")));
		WebElement messageText = driver.findElement(By.className("content-message"));
		Assertions.assertTrue(messageText.getText().contains("Note saved successfully"));

		//choose tab Notes one more
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();

		//check note displayed
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-note-title")));
		List<WebElement> displayedNotes = driver.findElements(By.className("content-note-title"));
		Assertions.assertEquals(displayedNotes.get(0).getText(), "test create note");
	}

	@Test
	public void testUpdateNote(){
		createNote("UPDATENOTE", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab Notes
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-edit-note")));
		List<WebElement> listBtnEditNote = driver.findElements(By.className("btn-edit-note"));
		WebElement btnEditNote = listBtnEditNote.get(0);
		btnEditNote.click();

		//fill input
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.clear();
		inputNoteTitle.sendKeys("test update note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit-modal")));
		WebElement noteSubmit = driver.findElement(By.id("note-submit-modal"));
		noteSubmit.click();

		//check updated successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-message")));
		WebElement messageText = driver.findElement(By.className("content-message"));
		Assertions.assertTrue(messageText.getText().contains("Note updated successfully"));

		//choose tab Notes one more
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();

		//check note displayed
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-note-title")));
		List<WebElement> displayedNotes = driver.findElements(By.className("content-note-title"));
		Assertions.assertEquals(displayedNotes.get(0).getText(), "test update note");
	}

	@Test
	public void testDeleteNote (){
		createNote("DELETENOTE", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab Notes
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();

		//btn delete note first
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-delete-note")));
		List<WebElement> listBtnDeleteNote = driver.findElements(By.className("btn-delete-note"));
		WebElement btnDeleteNote = listBtnDeleteNote.get(0);
		btnDeleteNote.click();

		Alert alert = driver.switchTo().alert();
		alert.accept();

		//choose tab Notes one more
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		tabNote = driver.findElement(By.id("nav-notes-tab"));
		tabNote.click();

		//check deleted successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-message")));
		WebElement messageText = driver.findElement(By.className("content-message"));
		Assertions.assertTrue(messageText.getText().contains("Note deleted successfully"));

		//check note no longer displayed
		Assertions.assertTrue(driver.findElements(By.className("content-note-title")).isEmpty());
	}


	private void createCredential (String username, String password){
		doMockSignUp("Hieu","Ngo",username,password);
		doLogIn(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab Credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential")));
		WebElement btnAddCredential = driver.findElement(By.id("add-credential"));
		btnAddCredential.click();

		//fill input
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCredentialUrl = driver.findElement(By.id("credential-url"));
		inputCredentialUrl.sendKeys("https://facebook.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputCredentialUsername = driver.findElement(By.id("credential-username"));
		inputCredentialUsername.sendKeys("hieu123");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputCredentialPassword = driver.findElement(By.id("credential-password"));
		inputCredentialPassword.sendKeys("1234");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit-modal")));
		WebElement credentialSubmit = driver.findElement(By.id("credential-submit-modal"));
		credentialSubmit.click();

		//check create successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-message")));
		WebElement messageText = driver.findElement(By.className("content-message"));
		Assertions.assertTrue(messageText.getText().contains("Credential saved successfully"));
	}

	@Test
	public void testCreateCredential (){
		String username = "createCredential";
		String password = "123";
		createCredential(username,password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();

		//check credential displayed
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-credential-url")));
		List<WebElement> displayedCredential = driver.findElements(By.className("content-credential-url"));
		Assertions.assertEquals(displayedCredential.get(0).getText(), "https://facebook.com");

		//check password show encrypted
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-credential-password")));
		List<WebElement> passwordEncrypt = driver.findElements(By.className("content-credential-password"));
		Assertions.assertFalse(passwordEncrypt.get(0).getText().equals(password));
	}

	@Test
	public void testUpdateCredential(){
		String username = "updateCredential";
		String password = "123";
		createCredential(username, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab Notes one more
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();

		//click button edit credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-edit-credential")));
		List<WebElement> listBtnEditCreden = driver.findElements(By.className("btn-edit-credential"));
		listBtnEditCreden.get(0).click();

		//check data password decrypt
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement rawPassword = driver.findElement(By.id("credential-password"));
		Assertions.assertEquals("1234", rawPassword.getAttribute("value"));

		//edit data credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCredentialUrl = driver.findElement(By.id("credential-url"));
		inputCredentialUrl.clear();
		inputCredentialUrl.sendKeys("https://google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputCredentialUsername = driver.findElement(By.id("credential-username"));
		inputCredentialUsername.sendKeys("4");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputCredentialPassword = driver.findElement(By.id("credential-password"));
		inputCredentialPassword.sendKeys("5");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit-modal")));
		WebElement credentialSubmit = driver.findElement(By.id("credential-submit-modal"));
		credentialSubmit.click();

		//check update successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-message")));
		WebElement messageText = driver.findElement(By.className("content-message"));
		Assertions.assertTrue(messageText.getText().contains("Credential updated successfully"));

		//choose tab credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();

		//check display after updated
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-credential-url")));
		List<WebElement> displayedCredential = driver.findElements(By.className("content-credential-url"));
		Assertions.assertEquals(displayedCredential.get(0).getText(), "https://google.com");
	}

	@Test
	public void testDeleteCredential(){
		String username = "deleteCredential";
		String password = "123";
		createCredential(username,password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		//choose tab credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();

		//find credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-delete-credential")));
		List<WebElement> listBtnDeleteCredential = driver.findElements(By.className("btn-delete-credential"));
		WebElement btnFirstCredentialItem = listBtnDeleteCredential.get(0);
		btnFirstCredentialItem.click();

		//confirm delete
		Alert alert = driver.switchTo().alert();
		alert.accept();

		//choose tab credential one more
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		tabCredential = driver.findElement(By.id("nav-credentials-tab"));
		tabCredential.click();

		//check deleted successfully
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-message")));
		WebElement messageText = driver.findElement(By.className("content-message"));
		Assertions.assertTrue(messageText.getText().contains("Credential deleted successfully"));

		//check note no longer displayed
		Assertions.assertTrue(driver.findElements(By.className("content-credential-url")).isEmpty());
	}
}
