package dav.AutogestionServicios;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageAutogestionServicioMiddle extends PageInicioMiddle {
	// Permite recolectar la información de los servicios que aprobo tx
	// List<HashMap<String, String>> results = new ArrayList<>();
	// String txDate;

	PageAutogestionServicios pageAutogestionServicios = null;
	PageLoginPymes pageLogin = null;
	// PageAutogestionServicios pageAutogestionServicios = null;
	// ------------------------------------------------------lOCATORS
	// MIDDLE---------------------------------------------------------------
	// Locators Consulta de novedades
	By tipoDeNovedad = By.xpath("//*[@id='cphCuerpo_ddlTipoNovedad']");
	By fechaNovedadDesde = By.xpath("//*[@id='cphCuerpo_txtFechaInicial']");
	By fechaNovedadHasta = By.xpath("//*[@id='cphCuerpo_txtFechaFinal']");
	By tipoIdentificacion = By.xpath("//*[@id='cphCuerpo_ddlTipoIdentificacion']");
	By numeroIdentificacion = By.xpath("//*[@id='cphCuerpo_txtNumID']");
	By btnBuscarProcesos = By.xpath("//*[@id='cphCuerpo_btBuscar']");

	// Locators que validaran que se encuentre el registro de la novedad de los
	// servicios contratados
	By registroNovedadCrear = By
			.xpath("//*[@id='cphCuerpo_pnNovedad']/table/tbody/tr/td/span[contains(text(),'Crear un Convenio')]");

	// Locators que validaran que se encuentre el registro de la novedad de los
	// servicios eliminados
	By registroNovedadEliminar = By
			.xpath("//*[@id='cphCuerpo_pnNovedad']/table/tbody/tr/td/span[contains(text(),'Eliminar un Convenio')]");

	// Locator que permite identificar si la información coincide con la pantalla de
	// la novedad
	String regNovedadServicio = "//*[@id='cphCuerpo_TaDataPosConvenio']/tbody/tr/td/table/tbody/tr/td/span[contains(text(),'SERVICIO')]";

	By tablaContenido = By.xpath("//*[@id='cphCuerpo_gvActividadesAdministradores']/tbody/tr");

	// Locator de botón regresar de la ventana donde se encuentra la novedad del
	// servicio
	By btnRegresar = By.xpath("//*[@id='cphCuerpo_btRegresar']");

	// Locators Servicios Contratados
	By clEmpresarial = By.id("cphCuerpo_txtClienteEmpresarial");
	By numIdentificacion = By.id("cphCuerpo_txtNumIdentificacion");
	By btnBuscarProceso = By.id("cphCuerpo_btBuscar");
	By tablaServContratados = By.id("cphCuerpo_pnlResultadoBusqueda");

	By btnImprimirLista = By.id("cphCuerpo_btImprimir");
	By btnGuardar = By.id("cphCuerpo_btGuardar");

	// Locators Botones Imprimir Lista
	By btnImprimir = By.id("idPrint");
	By btnSalir = By.xpath("//*[@id='form1']/table[1]/tbody/tr/td[3]/a");

	// Locators para obtener el nombre del servicio en la novedad
	By antNomServicioConv = By.id("cphCuerpo_LbAntNomServicioConv");
	By sapNombreServicio = By.id("cphCuerpo_TblSapNombreServicio");
	By posTNomServicioConv = By.id("cphCuerpo_LbPosTNomServicioConv");

	// Locators Administración empresas
	By numClienteEmp = By.id("cphCuerpo_txtBQNumeroClienteEmpresarial");
	By numIdEmpresa = By.id("cphCuerpo_txtBQNumIdentificacionEmpresa");
	By nombEmpresa = By.id("cphCuerpo_txtBQNombreEmpresa");

	By btnBuscEmpresa = By.id("cphCuerpo_btnQBBuscar");

	By resultEmpresa = By.xpath("//*[@id='cphCuerpo_gvEmpresa']/tbody");

	By cantidadTxAcumuladasEmp = By.xpath(
			"//*[@id=\"cphCuerpo_pnlDatosEmpresa\"]/table/tbody//td/b[contains(text(),'Cantidad Transacciones Acumuladas Empresa')]/../../td/span");

	String nombreNovedadServicio;

	String detalle;
	List<WebElement> xpaht = null;

	public PageAutogestionServicioMiddle(PageLoginPymes parentPage) {
		super(parentPage);
		this.pageAutogestionServicios = new PageAutogestionServicios(parentPage);
	}

	public void ConsultaNovedadesMiddle(String dateTx, List<HashMap<String, String>> info) throws Exception {
		Reporter.reportEvent(Reporter.MIC_INFO, "Realizando consulta de novedades en portal Middle...");

		String aprobarTx = SettingsRun.getTestData().getParameter("Aprobar Tx");

		if (dateTx != null && !info.isEmpty()) {

			// Verificar si resultadoContratarEliminarServicios es nulo Obtener la lista de
			// servicios procesados Verificar si la lista no es nula Recorrer la lista e
			// imprimir información
			int contarContratar = 0;
			int contarEliminar = 0;

			for (HashMap<String, String> op : info) {
				String tipoPrueba = op.get("tipoPruebaAprobada");

				if ("Contratar".equals(tipoPrueba)) {
					contarContratar++;
				} else if ("Eliminar".equals(tipoPrueba)) {
					contarEliminar++;
				}
			}

			DXCUtil.wait(2);
			if (aprobarTx.equals("Aprobar")) {
				Reporter.reportEvent(Reporter.MIC_INFO, "Se realizara toma de evidencias de las siguientes novedades: "
						+ info + ",\n fecha de Tx: " + dateTx);

				this.irAOpcion(null, "Informes", "Actividades Administradores");
				Reporter.reportEvent(Reporter.MIC_INFO,
						"Se ingreso a la ruta: Informes/Actividades Administradores...");
				Evidence.save("middle");

				if (contarEliminar != 0) {
					Reporter.reportEvent(Reporter.MIC_PASS,
							"se encontaró la siguiente cantidad de servicios eliminados: " + contarEliminar);
					DXCUtil.wait(2);
					procesarTipo("Eliminar un Convenio", dateTx);

				} else {
					Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron servicios eliminados");
				}

				if (contarContratar != 0) {
					Reporter.reportEvent(Reporter.MIC_PASS,
							"se encontaró la siguiente cantidad de servicios contratados: " + contarContratar);
					DXCUtil.wait(2);
					procesarTipo("Crear un Convenio", dateTx);

				} else {
					Reporter.reportEvent(Reporter.MIC_INFO, "No se encontraron servicios contratados");
				}

				this.imprimirResultados();

			} else if (aprobarTx.equals("Cancelar")) {
				this.adminEmpresas(contarContratar, contarEliminar);
				Reporter.reportEvent(Reporter.MIC_PASS, "Se evidencio corectamente la información solicitada");
			}
			DXCUtil.wait(2);
		} else {
			this.imprimirResultados();
			Reporter.reportEvent(Reporter.MIC_PASS,
					"No hay información para ser consultada, no hay transacciones acumuladas.");
		}

	}

	private void adminEmpresas(int contarContratar, int contarEliminar) throws Exception {

		Reporter.reportEvent(Reporter.MIC_PASS,
				"Total transacciones acumuladas en esta transacción: " + (contarContratar + contarEliminar));
		String clienteEmp = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		String empresaNIT = SettingsRun.getTestData().getParameter("NIT Empresa").trim();
		String nombreEmpresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();

		this.irAOpcion(null, "Administración", "Empresa");
		Evidence.saveFullPage("Administración de empresas", this);
		String data[] = { clienteEmp, empresaNIT, nombreEmpresa };
		By fields[] = { numClienteEmp, numIdEmpresa, nombEmpresa };
		DXCUtil.wait(2);
		this.ingresarDatos(getDriver(), fields, data);
		Evidence.save("Ingresar datos de consulta");
		if (this.element(btnBuscEmpresa) != null) {
			this.click(this.findElement(btnBuscEmpresa));
		}

		Evidence.saveFullPage("Resultados encontrados", this);

		DXCUtil.wait(2);

		String resultado = "//*[@id='cphCuerpo_gvEmpresa']/tbody//td[contains(text(),'" + clienteEmp
				+ "')]/../td[contains(text(),'" + empresaNIT + "')]/../td[contains(text(),'" + nombreEmpresa + "')]";
		String btnEditar = "//*[@id='cphCuerpo_gvEmpresa']/tbody//td[contains(text(),'" + clienteEmp
				+ "')]/../td[contains(text(),'" + empresaNIT + "')]/../td[contains(text(),'" + nombreEmpresa
				+ "')]/../td/a[contains(text(),'Editar')]";
		List<WebElement> tableResults = this.findElements(resultEmpresa);

		for (WebElement elemento : tableResults) {
			WebElement usuario = this.findElement(By.xpath(resultado));
			if (usuario.isDisplayed()) {
				this.findElement(By.xpath(btnEditar)).click();
				DXCUtil.wait(5);
				Evidence.save("Evidencia Administracion de usuarios");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró ningun resultado.");
				Evidence.save("Evidencia Administracion de usuarios");
			}
		}
		DXCUtil.wait(3);
		WebElement txAcumulada = this.findElement(cantidadTxAcumuladasEmp);
		String acumulamiento = txAcumulada.getText();
		Reporter.reportEvent(Reporter.MIC_PASS,
				"Total Cantidad de transacciones acumuladas empresa luego de realizar la contratación y eliminación de servicios: "
						+ acumulamiento);
		Evidence.saveFullPage("Evidencia Administracion de empresas", this);

	}

	// Este Metodo nos permite recibir la novedad que se quiere consultar e ingresar
	// la información para la consulta
	private void procesarTipo(String tipoNovedad, String dateTx) throws Exception {
		String typeId = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String numId = SettingsRun.getTestData().getParameter("Id usuario").trim();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		Date current = new Date();
		String currentDate = dateFormat.format(current);

		// Variable que toma la informaci�n del excel volviendolo en lista
		String[] data = { currentDate, currentDate, numId };
		// variable donde toma los campos y los vuelve una lista
		By[] fields = { fechaNovedadDesde, fechaNovedadHasta, numeroIdentificacion };

		DXCUtil.wait(1);
		// se seleccionan las opciones
		this.selectListItem(tipoDeNovedad, tipoNovedad);
		this.selectListItem(tipoIdentificacion, typeId);

		// metodo ingresa los datos en los campos
		ingresarDatos(getDriver(), fields, data);

		this.click(btnBuscarProcesos);
		Evidence.saveFullPage("Registro de novedad", this);
		DXCUtil.wait(1);

		// lista para almacenar los xpath de las novedades que coinciden con fecha de la
		// transacción
		List<String> xpathDetalles = new ArrayList<>();
		// String el cual reemplazara "%s" con dateTx para que verifique luego el xpath
		// existente
		String filasConFecha = String.format(
				"//*[@id='cphCuerpo_gvActividadesAdministradores']/tbody/tr/td[5][contains(text(),'%s')]/../td[7]/a",
				dateTx);
		// ira almacenando los xpath que coinciden con la fecha
		List<WebElement> novedades = this.findElements(By.xpath(filasConFecha));
		// Recorre la lista novedades para ir obteniendo el número de la novedad a la
		// cual se ingresara
		for (WebElement novedad : novedades) {
			xpathDetalles.add(getXpathDinamico(novedad.getText()));
		}

		DXCUtil.wait(2);
		// Recorrera xpathDetalles ya con todos las novedades encontradas que coinciden
		// con la misma fecha
		for (String xpathDetalle : xpathDetalles) {
			String numeroDetalle = obtenerNumeroDetalle(xpathDetalle);

			this.findElement(By.xpath(xpathDetalle)).click();
			DXCUtil.wait(2);
			Evidence.saveFullPage("Registro de novedad", this);
			Reporter.reportEvent(Reporter.MIC_PASS, "Se capturo correctamete la evidencia de la novedad: "
					+ numeroDetalle + " - Tipo de novedad consultada: " + tipoNovedad);

			DXCUtil.wait(2);
			findElement(btnRegresar).click();

		}

		Reporter.reportEvent(Reporter.MIC_PASS, "Se capturo correctamete las evidencias.");

	}

	// Metodo para imprimir Servicios contratados
	public void imprimirResultados() throws Exception {
		String clienteEmp = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		String empresaNIT = SettingsRun.getTestData().getParameter("NIT Empresa").trim();
		String guardarArchivo = SettingsRun.getTestData().getParameter("Guardar Resultados").trim();

		DXCUtil.wait(2);
		this.irAOpcion(null, "Informes", "Servicios Contratados");
		Reporter.reportEvent(Reporter.MIC_INFO, "Se ingreso a la ruta: Informes/Servicios Contratados...");

		String[] dataSC = { clienteEmp, empresaNIT };
		By[] fieldsSC = { clEmpresarial, numIdentificacion };

		this.ingresarDatos(getDriver(), fieldsSC, dataSC);
		DXCUtil.wait(2);
		this.findElement(btnBuscarProcesos).click();
		Evidence.saveFullPage("Evidencias imprimir archivo", this);

		if (guardarArchivo.equals("Imprimir")) {
			DXCUtil.wait(3);
			WebElement imprimirLista = this.findElement(btnImprimirLista);
			imprimirLista.click();
			DXCUtil.wait(2);
			Reporter.reportEvent(Reporter.MIC_INFO, "Entró a la ventana para imprimir la lista.");

			this.pageLogin = new PageLoginPymes(this);
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
			this.pageLogin.maximizeBrowser();

			DXCUtil.wait(2);
			Evidence.save("Evidencias imprimir archivo");
			this.findElement(btnImprimir).click();
			Evidence.saveFullPage("Evidencias imprimir archivo", this);

			// Nombre del archivo que va a aguardar
			String nombreArchivo = Evidence.getNbEvidenceDirectory() + "_Lista_ServiciosContratados";
			this.toPrint(nombreArchivo);

			DXCUtil.wait(2);
			this.findElement(btnSalir).click();

			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
			Reporter.reportEvent(Reporter.MIC_PASS, "Se imprimió correctamete.");
			Evidence.saveFullPage("Evidencias información de servicios contratados", this);
			this.closeCurrentBrowser();
			SettingsRun.exitTestIteration();

		} else if (guardarArchivo.equals("Excel")) {
			DXCUtil.wait(3);
			this.findElement(btnGuardar).click();
			Evidence.saveFullPage("Evidencias Servicios gestionados", this);
			Reporter.reportEvent(Reporter.MIC_PASS, "Se guardo el excel correctamente...");
			SettingsRun.exitTestIteration();
		}
	}

	// Método que permite reemplazar la expresión "%s" por el número del detalle en
	// el xpath
	private String getXpathDinamico(String txtnovedad) {
		return String.format(
				"//*[@id='cphCuerpo_gvActividadesAdministradores']/tbody/tr/td[7]/a[contains(text(),'%s')]",
				txtnovedad);
	} 

	// Método que permite encontrar el número del detalle al cual se le realizara la
	// toma de evidencias
	private String obtenerNumeroDetalle(String txtNovedad) {
		String regex = "\\((\\d+)\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(txtNovedad);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "Número de detalle no encontrado";
		}
	}

	// Permite ingresar los datos en cada campo
	private static void ingresarDatos(WebDriver driver, By[] fields, String[] data) throws Exception {
		if (fields.length != data.length) {
			Reporter.reportEvent(Reporter.MIC_WARNING, "La cantidad de campos y datos no coinciden");
			return;
		}

		for (int i = 0; i < fields.length; i++) {
			WebElement field = driver.findElement(fields[i]);
			if (field.isDisplayed()) {

				field.clear();
				DXCUtil.wait(2);
				field.sendKeys(data[i]);
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"No se encontraron los campos: " + field + ", por favor verifique la información.");
				SettingsRun.exitTestIteration();
			}
		}
		Evidence.save("Ingreso de datos");
	}

	// Método que permite Imprimir la lista de los resultados
	public static void toPrint(String nombreArchivo) throws Exception {

		// Permite copiar el nombre del archivo en el clipboard
		StringSelection stringSelection = new StringSelection(nombreArchivo);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		// Simula las teclas Ctrl+V para pegar la ubicación del archivo en el diálogo
		// del explorador de archivos
		// Copia la ubicación del archivo al portapapeles
		Robot robot = new Robot();
		//
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		DXCUtil.wait(3);
		try {
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			DXCUtil.wait(5);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (Exception e) {
			Reporter.reportEvent(Reporter.MIC_FAIL, e.getMessage());
		}
	}
}
