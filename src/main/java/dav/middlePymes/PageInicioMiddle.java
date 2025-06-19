package dav.middlePymes;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dav.transversal.DatosEmpresarial;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
import dxc.util.DXCUtil;

public class PageInicioMiddle extends PagePortalPymes {

//=======================================================================================================================
	// LOCATORS Y EXPRESIONES PARA ENCONTRAR LOS OBJETOS EN MIDDLE PYME
	String xPathLocMenu = "//span[@id='lblMasterMenu']//a[contains(text(), 'NB_MENU_INICIAL')]//parent::td";
	String xPathLocSubMenu = "//td[@class='easyMenuItemContentCell']/a[text()='NB_SUBMENU']//parent::td";
	String xPathLocSubMenu2 = "//td[@class='easyMenuItemContentCell']/a[contains(text(), 'NB_SUBMENU')]//parent::td";
	By locTitle = By.id("lblMasterTitulo");// "id:=lblMasterTitulo"; // TagName = "span"

	protected PageLoginPymes pageLogin;
	private boolean estaEnLogueo;

//=======================================================================================================================
	/**
	 * Este es el único constructor que tiene esta [BasePageWeb] ya que por sí sola
	 * no puede existir, debe depender de otra [BasePageWeb] que ya haya sido
	 * abierta y que direccione a ésta.
	 */
	public PageInicioMiddle(PageLoginPymes parentPage) {
		super(parentPage);
		this.pageLogin = parentPage;
	}

//***********************************************************************************************************************
	/**
	 * Método que permite moverse a través de los menúes presentados en Middle.
	 * Abriendo el menú dado por [opcMenu] y siguiendo la ruta por las opciones
	 * enviadas en [opcSubMenu], dando click en el último elemento de [opcSubMenu],
	 * en caso que no hayan sido enviados, da click directamente en [opcMenu].<br>
	 * [title] puede ser [null], VACIO o un valor; cuando se envía valor este método
	 * espera a que el título enviado se presente en pantalla <b>OJO</b> sólo si el
	 * título corresponde a un elemento con ID 'lblMasterTitulo'.<br>
	 * El retorno es [null] si se pudo ir a la opción deseada, en caso contrario
	 * envía un error, los errores que se pueden presentar, son porque no se
	 * encuentran las opciones enviadas.
	 */
	public String irAOpcion(String title, String opcMenu, String... opcSubMenu) throws Exception {
		DXCUtil.wait(1);
		String xPath = xPathLocMenu.replace("NB_MENU_INICIAL", opcMenu);
		String pathMenu = opcMenu;
		String finalOption = opcMenu;
		WebElement elemtMenu = this.element(xPath);
		if (elemtMenu == null) {
//			Evidence.save("ErrorMenu", this);
			Evidence.save("ErrorMenu");
			return "No se encontró en el Menú [" + pathMenu + "] - Valide la información";
		}
//-----------------------------------------------------------------------------------------------------------------------
		// SI LLEGA A ESTE PUNTO PUEDE IR SELECCIONANDO LAS OTRAS OPCIONES
		String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
		if (navegador.contains("CHROME")) {
			this.mouseOver(elemtMenu);

		} else {
			this.getJse().executeScript(
					"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
					elemtMenu);

		}

		List<WebElement> listaElements;
		for (int numOpc = 0; numOpc < opcSubMenu.length; numOpc++) {
			DXCUtil.wait(1);

			xPath = xPathLocSubMenu2.replace("NB_SUBMENU", opcSubMenu[numOpc]);
			listaElements = this.findElements(By.xpath(xPath));

			if (listaElements.size() > 1) {
				xPath = "(" + xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu[numOpc] + "    ") + ")[1]";

				listaElements = this.findElements(By.xpath(xPath));
			}
			if (listaElements.size() == 0) {
//				Evidence.save("ErrorMenu", this);
				Evidence.save("ErrorMenu");
				return "En el menú [" + pathMenu + "] NO se encontró la opción [" + opcSubMenu[numOpc]
						+ "] - Valide la información";
			} else if (listaElements.size() > 1)
				return "Existen muchos submenús que contienen [" + opcSubMenu[numOpc] + "] en el WebPage";

			do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
				DXCUtil.wait(1);
			} while (!this.isDisplayed(listaElements.get(0)));
			pathMenu += "/" + opcSubMenu[numOpc];
			finalOption = opcSubMenu[numOpc];
			// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
			// [MoveTargetOutOfBoundsException]

			if (navegador.contains("CHROME")) {
				DXCUtil.wait(3);
				this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
			} else {

				WebElement elemento = this.findElement(By.xpath(xPath));
				this.getJse().executeScript(
						"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
						elemento);
				this.getJse().executeScript("arguments[0].click();", elemento);

			}
//			this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
		}

		if (navegador.contains("CHROME")) {
			this.mouseClick();
		}
//-----------------------------------------------------------------------------------------------------------------------
		this.estaEnLogueo = false; // VALOR POR DEFECTO PORQUE NO DEBERÍA ESTAR EN LOGUEO
//-----------------------------------------------------------------------------------------------------------------------
		// ESPERA LA MUESTRA DEL TÍTULO [title]
		if (title != null && !title.isEmpty())
			this.waitIngresoModulo(title);
		else
			this.retornoALogueo();
//-----------------------------------------------------------------------------------------------------------------------
		if (this.estaEnLogueo) { // SE CERRÓ EL PORTAL
			DatosEmpresarial.ESTALOGMIDD_PYM = false; // PARA QUE INTENTE HACER EL LOGUEO
			this.pageLogin.loginMiddle(); // INTENTA LOGUEARSE DE NUEVO
			return this.irAOpcion(title, opcMenu, opcSubMenu); // INTENTA IR DE NUEVO A LA OPCIÓN REQUERIDA
		}
//		Evidence.save("IngresoA_" + finalOption.replace(" ", "_"), this);
		Evidence.save("IngresoA_" + finalOption.replace(" ", "_"));
		return null;
	}

	// ***********************************************************************************************************************
	/**
	 * Método que permite moverse a través de los menúes presentados en Middle.
	 * Abriendo el menú dado por [opcMenu] y siguiendo la ruta por las opciones
	 * enviadas en [opcSubMenu], dando click en el último elemento de [opcSubMenu],
	 * en caso que no hayan sido enviados, da click directamente en [opcMenu].<br>
	 * [title] puede ser [null], VACIO o un valor; cuando se envía valor este método
	 * espera a que el título enviado se presente en pantalla <b>OJO</b> sólo si el
	 * título corresponde a un elemento con ID 'lblMasterTitulo'.<br>
	 * El retorno es [null] si se pudo ir a la opción deseada, en caso contrario
	 * envía un error, los errores que se pueden presentar, son porque no se
	 * encuentran las opciones enviadas.
	 */
	public String irAOpcionMiddle(String title, String opcMenu, String... opcSubMenu) throws Exception {
		DXCUtil.wait(1);
		String xPath = xPathLocMenu.replace("NB_MENU_INICIAL", opcMenu);
		String pathMenu = opcMenu;
		String finalOption = opcMenu;
		WebElement elemtMenu = this.element(xPath);
		if (elemtMenu == null) {
//				Evidence.save("ErrorMenu", this);
			Evidence.save("ErrorMenu");
			return "No se encontró en el Menú [" + pathMenu + "] - Valide la información";
		}
		// -----------------------------------------------------------------------------------------------------------------------
		// SI LLEGA A ESTE PUNTO PUEDE IR SELECCIONANDO LAS OTRAS OPCIONES
		String navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

		if (navegador.contains("CHROME")) {
			DXCUtil.wait(2);
			this.mouseOver(elemtMenu);

		} else {
			this.getJse().executeScript(
					"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
					elemtMenu);

		}

		List<WebElement> listaElements;
		if (opcSubMenu != null) {
			for (int numOpc = 0; numOpc < opcSubMenu.length; numOpc++) {
				DXCUtil.wait(1);
				if (opcSubMenu[numOpc] != null && !opcSubMenu[numOpc].isEmpty()) {
					xPath = xPathLocSubMenu2.replace("NB_SUBMENU", opcSubMenu[numOpc]);
					listaElements = this.findElements(By.xpath(xPath));

					if (listaElements.size() > 1) {
						xPath = "(" + xPathLocSubMenu.replace("NB_SUBMENU", opcSubMenu[numOpc] + "    ") + ")[1]";

						listaElements = this.findElements(By.xpath(xPath));
					}
					if (listaElements.size() == 0) {
//					Evidence.save("ErrorMenu", this);
						Evidence.save("ErrorMenu");
						return "En el menú [" + pathMenu + "] NO se encontró la opción [" + opcSubMenu[numOpc]
								+ "] - Valide la información";
					} else if (listaElements.size() > 1)
						return "Existen muchos submenús que contienen [" + opcSubMenu[numOpc] + "] en el WebPage";

					do { // ESPERA A QUE SE HAYA DESPLEGADO EL ELEMENTO
						DXCUtil.wait(1);
					} while (!this.isDisplayed(listaElements.get(0)));
					pathMenu += "/" + opcSubMenu[numOpc];
					finalOption = opcSubMenu[numOpc];
					// NO SE PUEDE CON [listaElements.get(0)] PORQUE SE PRESENTA UN
					// [MoveTargetOutOfBoundsException]

					if (navegador.contains("CHROME")) {
						DXCUtil.wait(3);
						this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO

					} else {

						WebElement elemento = this.findElement(By.xpath(xPath));
						this.getJse().executeScript(
								"var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window}); arguments[0].dispatchEvent(event);",
								elemento);
						this.getJse().executeScript("arguments[0].click();", elemento);

					}

				}
//				this.mouseOver(this.element(xPath)); // POSICIONA EL MOUSE SOBRE EL ELEMENTO RESPECTIVO
			}
		}
		if (navegador.contains("CHROME")) {
			this.mouseClick();
		}
		// -----------------------------------------------------------------------------------------------------------------------
		this.estaEnLogueo = false; // VALOR POR DEFECTO PORQUE NO DEBERÍA ESTAR EN LOGUEO
		// -----------------------------------------------------------------------------------------------------------------------
		// ESPERA LA MUESTRA DEL TÍTULO [title]
		if (title != null && !title.isEmpty())
			this.waitIngresoModulo(title);
		else
			this.retornoALogueo();
		// -----------------------------------------------------------------------------------------------------------------------
		if (this.estaEnLogueo) { // SE CERRÓ EL PORTAL
			DatosEmpresarial.ESTALOGMIDD_PYM = false; // PARA QUE INTENTE HACER EL LOGUEO
			this.pageLogin.loginMiddle(); // INTENTA LOGUEARSE DE NUEVO
			return this.irAOpcionMiddle(title, opcMenu, opcSubMenu); // INTENTA IR DE NUEVO A LA OPCIÓN REQUERIDA
		}
//			Evidence.save("IngresoA_" + finalOption.replace(" ", "_"), this);
		Evidence.save("IngresoA_" + finalOption.replace(" ", "_"));
		return null;
	}

//***********************************************************************************************************************
	/**
	 * Metodo que da click en la opcion de menú 'Inicio'.
	 */
	public void irAInicio() throws Exception {
		this.irAOpcion(null, "Inicio");
	}

//***********************************************************************************************************************
	/**
	 * Método que espera a que se presente el TÍTULO [titleModulo].<br>
	 * Se presenta Excepción si supera los 60 segundos de espera.<br>
	 * Este método sirve sólo cuando el título corresponde al estándar Middle que no
	 * tiene que moverse a ningún frame y buscar el elemento cuyo ID sea
	 * "lblMasterTitulo"
	 */
	public void waitIngresoModulo(String titleModulo) throws Exception {

		int time = 1;
		boolean esTitleEsperado = false;
		WebElement elemTitle;
		titleModulo = titleModulo.trim();
		do {
			DXCUtil.wait(1);
			elemTitle = this.element(locTitle);
			if (elemTitle != null)
				esTitleEsperado = DXCUtil.equalsIgnoreCaseAndAccents(titleModulo, elemTitle.getText().trim());

			if (!esTitleEsperado && this.retornoALogueo())
				return; // TERMINA EL CICLO

			time++;
		} while (!esTitleEsperado && time <= 60);
		if (!esTitleEsperado) {
			throw new Exception(
					"PageInicioMiddle ERROR -- Se supera los 30 segundos esperando ingreso al módulo con título ["
							+ titleModulo + "]");
		}
	}

//***********************************************************************************************************************
	/**
	 * Este método retorna [true] si la pantalla se encuentra en la de logueo, como
	 * que se salió del portal Middle.<br>
	 * Adicional deja almacenado en [estaEnLogueo] el valor respectivo.
	 */
	private boolean retornoALogueo() {
		WebElement objTituloInicio = this.element("//td[@class='Titulo']");
		this.estaEnLogueo = false;
		if (this.isDisplayed(objTituloInicio))
			this.estaEnLogueo = objTituloInicio.getText().contains("Bienvenido al Portal PYMES");
		return this.estaEnLogueo;
	}
//***********************************************************************************************************************
}