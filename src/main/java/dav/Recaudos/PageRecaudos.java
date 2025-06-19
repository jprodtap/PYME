package dav.Recaudos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import dav.pymes.moduloCrearTx.ControllerDestinosMasivos;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.DataDriven;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;
import net.bytebuddy.asm.Advice.Return;
import dav.inscripciones.PageInscripcionesCuenta;
import dav.pymes.PageLoginPymes;

public class PageRecaudos extends BasePageWeb {

	// locators--------
	By ConveniodeRecaudo = By.xpath("//*[@id='cphCuerpo_ddlConvenioRecaudo']");
	By NombreProceso = By.xpath("//*[@id='cphCuerpo_txtNombre']");
	By Referencia1 = By.xpath("//*[@id='cphCuerpo_txtReferencia']");
	By Referencia2 = By.xpath("//*[@id='cphCuerpo_txtReferencia2']");
	By ValorPago = By.xpath("//*[@id='cphCuerpo_txtValorPagar']");
	By fechaLimite = By.xpath("//*[@id='cphCuerpo_txtFechaHasta']");
	By agregarReferencia = By.xpath("//*[@id='cphCuerpo_btnAgregar']");
	By tablaRef = By.xpath("//*[@id='cphCuerpo_pnlReferencias']/table/tbody/tr[1]/td");
	By continuarRef = By.xpath("//*[@id='cphCuerpo_btnContinuar']");
	By TablaReferenciasR = By.xpath("//*[@id='cphCuerpo_gvNominas']");
	String Checkbox = "//td[contains(text(),'NombreDelProceso/Numero')]//preceding-sibling::td/input";
	By AprobarRef = By.xpath("//*[@id='cphCuerpo_btnAprobar']");
	By Mostrardatos = By.xpath("//*[@id='cphCuerpo_btnMostrarDatos']");
	By msgAlerta = By.xpath("//*[@id='lblMasterAlerta']");
	By tablaReferencias = By.xpath("//*[@id='cphCuerpo_pnlResultadoUnificadoRecaudos']//table//tbody");
	By eliminar = By.xpath("//*[@id='cphCuerpo_btnEliminar']");
	By EliminarTodos = By.xpath("//*[@id='cphCuerpo_btnEliminarTodos']");
	By Modificar = By.xpath("//*[@id='cphCuerpo_btnModificar']");
	By TablarefBD = By.xpath("//*[@id='cphCuerpo_gvReferencias']");
	By TablarefArch = By.xpath("//*[@id=\"cphCuerpo_pnlReferencias\"]/table/tbody/tr[1]/td");
	By SaveCambios = By.xpath("//*[@id='cphCuerpo_btnAgregar']");
	By BtnCargarArchivo = By.xpath("//*[@id='cphCuerpo_file']");
	By numeroproceso = By.xpath("//*[@id='cphCuerpo_txtNumProceso']");
	By nombreproceso = By.xpath("//*[@id='cphCuerpo_txtNomProceso']");
	By tablaConsultaProcesos = By.xpath("//*[@id='cphCuerpo_gvConsulta']/tbody");
	By NumerosProcesos = By.xpath("//*[@id=\"cphCuerpo_gvConsulta\"]//a");
	By regresar = By.xpath("//*[@id='cphCuerpo_btREgresar']");
	By btnEstados = By.xpath("//*[@id='cphCuerpo_dllEstado']");
	By fechaDesde = By.xpath("//*[@id='cphCuerpo_txtFechaDesde']");
	By fechaHasta = By.xpath("//*[@id='cphCuerpo_txtFechaHasta']");
	By CambioFecha = By.xpath("//*[@id='cphCuerpo_RdCambioFecha']");
	By btnCambioFecha = By.xpath("//*[@id='cphCuerpo_btnActualizarFecha']");
	By fechaNueva = By.xpath("//*[@id='cphCuerpo_txtCambioFecha']");
	By CambioValor = By.xpath("//*[@id='cphCuerpo_RdCambioValor']");
	By btnCambioValor = By.xpath("//*[@id='cphCuerpo_btnCambiarValor']");
	By valorNuevo = By.xpath("//*[@id='cphCuerpo_txtCambiarValor']");
	By eliminarAdicionar = By.xpath("//*[@id='cphCuerpo_RdReferencia']");
	By tipoNovedadRef = By.xpath("//*[@id='cphCuerpo_dllTipoNovedad']");
	By BtnCargaArchivoNov = By.xpath("//*[@id='cphCuerpo_Fele_UpArchivo']");
	By btnSiguiente = By.xpath("//*[@id='cphCuerpo_lnkSiguiente']");
	//Variables
	public static String[] datos = null;
	String NumProceso = "//*[@id='cphCuerpo_pnlResultadoConsulta']//a[text()='NumProceso']";
	WebElement TablaConsultaPro = null;
	List<WebElement> Informacion = null;

	ControllerDestinosMasivos controllerDestinosMasivos = null;

	public PageRecaudos(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Constructor y Pages
	 */
	PageOrigen pageOrigen = null;

	static String Mensajes = null;

	
	//Metodo que nos lleva al page del submodulo dependiendo del tipo de Prueba
	public boolean RecaudosIr() throws Exception {
		Boolean msgMenu = false;
		DXCUtil.wait(2);
		String TipoP = SettingsRun.getTestData().getParameter("tipo de prueba").trim();
		this.pageOrigen = new PageOrigen(this);
		if (TipoP.contains("Nuevas Referencias")) {
			String ValidarOP = this.pageOrigen.irAOpcion(null, "Recaudo", "Nueva Base de Datos");
			msgMenu = true;
			Mensajes = "Redireccion al modulo exitosa";
//			Evidence.save("Pantalla Nueva BD", this);
			Evidence.save("Pantalla Nueva BD");
		}
		if (TipoP.contains("Consulta de Referencias")) {
			String ValidarOP = this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
			msgMenu = true;
			Mensajes = "Redireccion al modulo exitosa";
			Evidence.save("Pantalla consulta de Refereencias");
		}
		if (TipoP.contains("Aprobar Base de Datos")) {
			String ValidarOP = this.pageOrigen.irAOpcion(null, "Recaudo", "Aprobar Base de Datos");
			msgMenu = true;
			Mensajes = "Redireccion al modulo exitosa";
			Evidence.save("Pantalla Aprobacion BD");
		}
		if (TipoP.contains("Consulta de Procesos")) {
			String ValidarOP = this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Procesos Base de Datos");
			msgMenu = true;
			Mensajes = "Redireccion al modulo exitosa";
			Evidence.save("Pantalla Consulta de Procesos");
		}
		if (TipoP.contains("Novedades Base de Datos")) {
			String ValidarOP = this.pageOrigen.irAOpcion(null, "Recaudo", "Novedades Base de Datos");
			msgMenu = true;
			Mensajes = "Redireccion al modulo exitosa";
			Evidence.save("Pantalla Novedades BD");
		}

		if (TipoP == null || TipoP.equals("")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Tipo de prueba vacio o invalido");
			SettingsRun.exitTestIteration();
		}

		return msgMenu;

	}
	//Metodo que nos crea nuevas Referencias ya sean Digitadas o Con Cargue de archivo
	public boolean NuevasBD() throws Exception {
		//Retorno de la prueba
		Boolean msgReferencia = false;
		DXCUtil.wait(1);
		//Datos de Hoja de Data
		String Pruebap = SettingsRun.getTestData().getParameter("Aprobar/Eliminar/Modificar").trim();
		String TipoCargue = SettingsRun.getTestData().getParameter("Cargue Referencias").trim();
		String Convenio = SettingsRun.getTestData().getParameter("Codigo Convenio Empresa").trim();
		String nomProceso = SettingsRun.getTestData().getParameter("Nombre del Proceso").trim();
		String NumeroRef = SettingsRun.getTestData().getParameter("Numero de Referencias").trim();
		String NumeroRef1 = SettingsRun.getTestData().getParameter("Referencia 1").trim();
		String NumeroRef2 = SettingsRun.getTestData().getParameter("Referencia 2").trim();
		String valorPagar = SettingsRun.getTestData().getParameter("Valor a Pagar").trim();
		String FechaLim = SettingsRun.getTestData().getParameter("Fecha Limite(DD/MM/AA)").trim();
		//Escenario en el cual se realiza la creacion de referencias de manera digitada
		if (TipoCargue.contains("Digitado")) {
			selectListContainsItems(ConveniodeRecaudo, Convenio);
			this.write(NombreProceso, nomProceso);
			this.write(Referencia1, NumeroRef1);
			//Se Condiciona para recibir si una o dos referencias
			if (NumeroRef.contains("2")) {
				this.write(Referencia2, NumeroRef2);
			}
			this.write(ValorPago, valorPagar);
			this.write(fechaLimite, FechaLim);
			DXCUtil.wait(2);
			Evidence.save("Referencias Adicionadas");
			this.click(agregarReferencia);
			DXCUtil.wait(1);
			Evidence.save("Referencias Adicionadas");
			//Validacion de Referencias Recibida Exitosamente
			if (this.element(tablaRef) != null) {
				msgReferencia = true;
				Mensajes = "Se agrego correctamente la Referencia";
			} 
			//Validacion de Referencias No Recibidas Exitosamente
			else if (this.element(tablaRef) == null) {
				Mensajes = "No se agrego correctamente la Referencia";
			}
//			Escenario Donde Se aprueba el proceso de Aprobar las Referencias
			if (Pruebap.contains("Aprobar")) {
				DXCUtil.wait(1);
				this.click(continuarRef);
				Evidence.save("Aprobacion de la Base de Datos");
				//Se extrae la ifromacion de la tabla de BD y se selcciona el Proceso con el Nombre Asignado
				WebElement tablaResultados = this.findElement(TablaReferenciasR);
				List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
				List<String> textosObtenidos = new ArrayList<>();
				String informacionTabla = "";
				List<WebElement> celdas = null;
				WebElement checkboxsl = null;
				// se extrae la información de la tabla
				String xpathRemplazarProceso = "";
				xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
				checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
				//Selecion de lA BD Para Aprobar el proceso
				if (this.element(checkboxsl) != null) {
					this.click(checkboxsl);
					Reporter.reportEvent(Reporter.MIC_INFO, "Referencia de BD selecionada");
					Evidence.save("Referencia de BD selecionada");
				} else if (this.element(checkboxsl) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Referencia de BD no encontrada");
					msgReferencia = false;
					Mensajes = "Referencia de BD no encontrada";
					return msgReferencia;
				}
				DXCUtil.wait(1);
				this.click(AprobarRef);
				WebElement msgAlert = this.findElement(msgAlerta);
				if (this.element(msgAlert) != null) {
					Evidence.save("Base de datos Recibida exitosamente");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido la base de datos");
					Mensajes = this.element(msgAlert).getText();
				} else if (this.element(msgAlert) == null) {
					Evidence.save("Base de datos NO Recibida");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
					Mensajes = "Ah ocurrido un error al aprobar el proceso";
					msgReferencia = false;
				}
			} else if (Pruebap.contains("Eliminar Todos")) {
				DXCUtil.wait(1);
				this.click(EliminarTodos);
				DXCUtil.wait(1);
				Evidence.save("Mensaje de Eliminar Todos");
				this.acceptDialog();
				if (this.element(tablaRef) == null) {
					Evidence.save("Referencias Eliminadas");
					Mensajes = "Se elimino todos los registros";
				} else if (this.element(tablaRef) != null) {
					Evidence.save("Referencias NO Eliminadas");
					Mensajes = "NO se elimino todos los registros";
					msgReferencia = false;
				}
				
//				Escenario Donde Se aprueba el proceso de eliminar las Referencias Agregadas
			} else if (Pruebap.contains("Eliminar")) {
				DXCUtil.wait(1);
				WebElement tablaResultados = this.findElement(TablarefBD);
				List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
				List<String> textosObtenidos = new ArrayList<>();
				String informacionTabla = "";
				List<WebElement> celdas = null;
				WebElement checkboxsl = null;
				// se extrae la información de la tabla
				String xpathRemplazarNumRef = "";
				xpathRemplazarNumRef = Checkbox.replace("NombreDelProceso/Numero", NumeroRef1);
				checkboxsl = this.element(By.xpath(xpathRemplazarNumRef));
				//Selecion de lA BD Para Aprobar el proceso
				if (this.element(checkboxsl) != null) {
					this.click(checkboxsl);
					Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
					Evidence.save("Proceso de BD selecionado");
				} else if (this.element(checkboxsl) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
					msgReferencia = false;
					Mensajes = "Preceso de referencia no encontrado";
					return msgReferencia;
				}
				this.click(eliminar);
				DXCUtil.wait(1);
				Evidence.save("Mensaje de Eliminar");
				this.acceptDialog();
				WebElement msgAlert = this.findElement(msgAlerta);
				if (this.element(msgAlert) != null) {
					Evidence.save("Referencia Eliminada exitosamente");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha eliminado la Referencia");
					Mensajes = this.element(msgAlert).getText();
				} else if (this.element(msgAlert) == null) {
					Evidence.save("Referencia NO Eliminada exitosamente");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha Eliminado la Referencia");
					Mensajes = "Ah ocurrido un error al Eliminar la Referencia";
					msgReferencia = false;
				}
//				Escenario Donde Se aprueba el proceso de Modificar las Referencias Agregadas
			} else if (Pruebap.contains("Modificar")) {
				DXCUtil.wait(1);
				WebElement tablaResultados = this.findElement(TablarefBD);
				List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
				List<String> textosObtenidos = new ArrayList<>();
				String informacionTabla = "";
				List<WebElement> celdas = null;
				WebElement checkboxsl = null;
				// se extrae la información de la tabla
				String xpathRemplazarNumRef = "";
				xpathRemplazarNumRef = Checkbox.replace("NombreDelProceso/Numero", NumeroRef1);
				checkboxsl = this.element(By.xpath(xpathRemplazarNumRef));
				//Selecion de lA BD Para Aprobar el proceso
				if (this.element(checkboxsl) != null) {
					this.click(checkboxsl);
					Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
					Evidence.save("Proceso de BD selecionado");
				} else if (this.element(checkboxsl) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
					msgReferencia = false;
					Mensajes = "Preceso de referencia no encontrado";
					return msgReferencia;
				}
				this.focus(Modificar);
				this.click(Modificar);
				Evidence.save("Modificar");
				DXCUtil.wait(2);
				//Se realiza la Modificacion de las refrencias
				NumeroRef1 = NumeroRef1 + "1";
				valorPagar = valorPagar + "0";
				//Se modifica la fecha inicial por un año mas
				int anioActual = LocalDate.now().getYear() + 1;
				String Anio = Integer.toString(anioActual);

				String[] fechaLit = new String[3];
				fechaLit = FechaLim.split("/");
				fechaLit[2] = Anio;
				FechaLim = "";
				for (int i = 0; i < fechaLit.length; ++i) {
					FechaLim += fechaLit[i] + "/";
				}
				Evidence.save("Modificar datos");
				this.write(Referencia1, NumeroRef1);
				if (NumeroRef.contains("2")) {
					NumeroRef2 = NumeroRef2 + "1";
					this.write(Referencia2, NumeroRef2);
				}
				DXCUtil.wait(1);
				WebElement PagoModificadol = this.findElement(ValorPago);
				Actions action = new Actions(getDriver());
				// Obtiene el toolkit
				Toolkit toolKit = Toolkit.getDefaultToolkit();
				// Ayuda a guardar en el portapapeles
				Clipboard clipBoard = toolKit.getSystemClipboard();

				// esto representa que selecciono el texto
				StringSelection stringSelection = new StringSelection(valorPagar);
				// Copia en el portapapeles
				clipBoard.setContents(stringSelection, null);
				// se da click en el input en el que ingresaremos la informaci�n
				PagoModificadol.click();
				// Indicamos que es un(Control + a) para seleccionar el texto
				action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
				// Indicamos que es un(Control + a) para pegar el texto del
				// clipboard
				action.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();
				this.write(fechaLimite, FechaLim);
				Evidence.save("Datos ya Modificados");
				DXCUtil.wait(1);
				this.click(SaveCambios);
				Evidence.save("Datos ya Modificados");
				xpathRemplazarNumRef = Checkbox.replace("NombreDelProceso/Numero", NumeroRef1);
				checkboxsl = this.element(By.xpath(xpathRemplazarNumRef));
				if (this.element(checkboxsl) != null && this.element(checkboxsl).isEnabled()
						&& this.element(msgAlerta) == null) {
					WebElement TablaDatosRef = this.element(TablarefBD);
					List<WebElement> Informacion = TablaDatosRef.findElements(By.tagName(("tr")));
					datos = new String[Informacion.size()];
					for (int i = 0; i < datos.length; i++) {
						datos[i] = Informacion.get(i).getText();
					}
					for (int i = 0; i < datos.length; i++) {
						Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
					}
					Mensajes = "Se ha traido la informacion de la referencia Correctamente";
					Evidence.save("Modificacion Exitosa");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se realiza la modificacion Correctamente");
					DXCUtil.wait(1);
					this.click(continuarRef);
					Evidence.save("Aprobacion de la Base de Datos");
					tablaResultados = this.findElement(TablaReferenciasR);
					filas = tablaResultados.findElements(By.tagName("tr"));
					textosObtenidos = new ArrayList<>();
					informacionTabla = "";
					celdas = null;
					checkboxsl = null;
					// se extrae la información de la tabla
					String xpathRemplazarProceso = "";
					xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
					checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
					//Selecion de lA BD Para Aprobar el proceso
					if (this.element(checkboxsl) != null) {
						this.click(checkboxsl);
						Reporter.reportEvent(Reporter.MIC_INFO, "Referencia de BD selecionada");
						Evidence.save("Referencia de BD selecionada");
					} else if (this.element(checkboxsl) == null) {
						Reporter.reportEvent(Reporter.MIC_FAIL, "Referencia de BD no encontrada");
						msgReferencia = false;
						Mensajes = "Referencia de BD no encontrada";
						return msgReferencia;
					}
					DXCUtil.wait(1);
					this.click(AprobarRef);
					WebElement msgAlert = this.findElement(msgAlerta);
					if (this.element(msgAlert) != null) {
						Evidence.save("Base de datos Recibida exitosamente");
						Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido la base de datos");
						Mensajes = this.element(msgAlert).getText();
					} else if (this.element(msgAlert) == null) {
						Evidence.save("Base de datos NO Recibida");
						Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
						Mensajes = "Ah ocurrido un error al aprobar el proceso";
						msgReferencia = false;
					}
					msgReferencia = true;

				} else if (this.element(msgAlerta) != null) {
					WebElement msgAlert = this.element(msgAlerta);
					Mensajes = this.element(msgAlert).getText();
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se realiza la modificacion Correctamente");
					Evidence.save("Error Modificacion");
					msgReferencia = false;
				}

			}
			//Validacion si el campo en la hoja de Datos se encuentra Vacio
			if (Pruebap == null || Pruebap.equals("")) {
				Mensajes = "Selecione un tipo de prueba para las nuevas referencias";
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se identifica el tipo de prueba para las referencias");
				msgReferencia = false;
				return msgReferencia;

			}
			// Escenario En el cual se Agrega las referencion Con cargie de Archivo
		} else if (TipoCargue.contains("Archivo")) {
			String TipoApro = SettingsRun.getTestData().getParameter("Aprobar/Eliminar").trim();
			String ArchivoCarga = SettingsRun.getTestData().getParameter("Estructura");
			String ArchivoRuta = SettingsRun.getTestData().getParameter("Ruta Archivo Referencias");
			this.controllerDestinosMasivos = new ControllerDestinosMasivos(null);
			this.write(NombreProceso, nomProceso);
			//Tipo de Archivo 1
			if (ArchivoCarga.contains("Excel 1 Referencia")) {
				//Se llama el metodo en el cual se extrae la ruta del archivo del la hoja de Datos y se crea segun el tipo de Cargue seleccionado
				Boolean CreacionArchivo = this.controllerDestinosMasivos.ReferenciasEcxel1(ArchivoRuta);
				if (CreacionArchivo) {
					Mensajes = "Se creo el Archivo correctamente";
					msgReferencia = true;

				} else {
					Mensajes = "Hubo un error en la creacion del archivo";
					msgReferencia = false;
				}
			}
			//Tipo de Archivo 2
			if (ArchivoCarga.contains("Excel 2 Referencia")) {
				//Se llama el metodo en el cual se extrae la ruta del archivo del la hoja de Datos y se crea segun el tipo de Cargue seleccionado
				Boolean CreacionArchivo = this.controllerDestinosMasivos.ReferenciasEcxel2(ArchivoRuta);
				if (CreacionArchivo) {
					Mensajes = "Se creo el Archivo correctamente";
					msgReferencia = true;
				} else {
					Mensajes = "Hubo un error en la creacion del archivo";
					msgReferencia = false;
				}
			}
			//Tipo de Archivo 3
			if (ArchivoCarga.contains("Archivo 130 Posiciones") || ArchivoCarga.contains("Archivo 50 Posiciones")) {
				//Se llama el metodo en el cual se extrae la ruta del archivo del la hoja de Datos y se crea segun el tipo de Cargue seleccionado
				Boolean CreacionArchivo = this.controllerDestinosMasivos.ReferenciasArchivoPlano130_50(ArchivoRuta);
				if (CreacionArchivo) {
					Mensajes = "Se creo el Archivo correctamente";
					msgReferencia = true;
				} else {
					Mensajes = "Hubo un error en la creacion del archivo";
					msgReferencia = false;
				}

			}
			this.mouseOver(BtnCargarArchivo);
			this.mouseClick();
			DXCUtil.wait(2);
			String rutaArchiv = SettingsRun.getTestData().getParameter("rutaArchivo");
			DXCUtil.cargueArchivo(rutaArchiv);
			DXCUtil.wait(2);
			Evidence.save("Cargue Archivo");
			int cont = 0;
			do {
				DXCUtil.wait(1);
				cont++;
			} while (this.element(TablarefArch) == null && cont <= 2);
			if (this.element(msgAlerta) != null) {
				Evidence.save("error Cargue");
				Mensajes = this.getText(msgAlerta);
				return msgReferencia = false;
			}
			Evidence.save("Archivo ya Cargado");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se ha cargado el archivo correctamente");
			DXCUtil.wait(2);
			this.focus(continuarRef);
			this.click(continuarRef);
			DXCUtil.wait(2);
			Evidence.save("Bases de Datos Por aprobar");
			WebElement tablaResultados = this.findElement(TablaReferenciasR);
			List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
			List<String> textosObtenidos = new ArrayList<>();
			String informacionTabla = "";
			List<WebElement> celdas = null;
			WebElement checkboxsl = null;
			// se extrae la información de la tabla
			String xpathRemplazarProceso = "";
//		    for (int i=1; i<filas.size();i++) {
//		        WebElement fila = filas.get(i);
//		        informacionTabla = fila.getText();
//		        textosObtenidos.add(informacionTabla);
			xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
			checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
			//Selecion de lA BD Para Aprobar el proceso
			if (this.element(checkboxsl) != null) {
				this.click(checkboxsl);
				Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
				Evidence.save("Proceso de BD selecionado");
				msgReferencia = true;
			} else if (this.element(checkboxsl) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
				msgReferencia = false;
				Mensajes = "Preceso de referencia no encontrado";
			}
			DXCUtil.wait(1);
//			Escenario Donde Se aprueba el proceso de las Referencias Agregadas por Archivo
			if (TipoApro.equals("Aprobar")) {
				this.click(AprobarRef);
				WebElement msgAlert = this.findElement(msgAlerta);
				if (this.element(msgAlert) != null) {
					Evidence.save("Base de datos Recibida exitosamente");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido el proceso");
					Mensajes = this.element(msgAlert).getText();
				} else if (this.element(msgAlert) == null) {
					Evidence.save("Base de datos NO Recibida");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
					Mensajes = "Ah ocurrido un error al aprobar el proceso";
					msgReferencia = false;
				}
			}
//			Escenario Donde Se Elimina el proceso de las Referencias Agregadas por Archivo
			if (TipoApro.equals("Eliminar")) {
				this.click(eliminar);
				DXCUtil.wait(1);
				Evidence.save("Mensaje de Eliminar");
				this.acceptDialog();
				DXCUtil.wait(1);
				if (this.element(msgAlerta) == null) {
					Evidence.save("Base de datos Eliminada exitosamente");
					Mensajes = "Se ha Eliminado el proceso";

				}
				if (this.element(msgAlerta) != null) {
					WebElement msgAlert = this.findElement(msgAlerta);
					Evidence.save("Base de datos NO eliminada");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha eliminado el proceso");
					Mensajes = "Ah ocurrido un error al eliminar el proceso";
					Mensajes = this.element(msgAlert).getText();
					msgReferencia = false;
				}
			}
			//Validacion si el campo en la hoja de Datos se encuentra Vacio
			if (TipoApro == null || TipoApro.equals("")) {
				Mensajes = "Selecione un tipo de prueba para las nuevas referencias de archivo";
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se identifica el tipo de prueba para las referencias");
				msgReferencia = false;
				return msgReferencia;

			}

		}

		return msgReferencia;
	}

	
	//Escenario Donde se consulta Refrerencias de la Empresa en la BD
	public boolean ConsultaReferencias() throws Exception {
		Boolean msgConsultaReferencia = false;
		String NumeroRef = SettingsRun.getTestData().getParameter("Numero de Referencias").trim();
		String NumeroRef1 = SettingsRun.getTestData().getParameter("Referencia 1").trim();
		String NumeroRef2 = SettingsRun.getTestData().getParameter("Referencia 2").trim();
		String Convenio = SettingsRun.getTestData().getParameter("Codigo Convenio Empresa").trim();
		DXCUtil.wait(2);
		selectListContainsItems(ConveniodeRecaudo, Convenio);
		this.write(Referencia1, NumeroRef1);
		if (NumeroRef.contains("2")) {
			this.write(Referencia2, NumeroRef2);
		}
		Evidence.save("Datos de la referencia a consultar");
		this.click(Mostrardatos);
		DXCUtil.wait(2);
		WebElement msgAlert = this.element(msgAlerta);
		if (msgAlert == null) {
			Evidence.save("Refererncia Consultada");
			Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Consultado la referencia correctamente");
			WebElement TablaDatosRef = this.element(tablaReferencias);
			List<WebElement> Informacion = TablaDatosRef.findElements(By.tagName(("tr")));
			datos = new String[Informacion.size()];
			for (int i = 0; i < datos.length; i++) {
				datos[i] = Informacion.get(i).getText();
			}
			for (int i = 0; i < datos.length; i++) {
				Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
			}
			Mensajes = "Se ha traido la informacion de la referencia Correctamente";
			msgConsultaReferencia = true;
		} else if (msgAlert != null) {
			Mensajes = this.element(msgAlert).getText();
			if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
				Evidence.save("Refererncia No Existe");
				msgConsultaReferencia = false;
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se ha encontrado la Referencia");
			}
		}
		return msgConsultaReferencia;
	}
// Escenario donde se Aprueban los procesos de las BD 
	public boolean AprobacionBaseDatos() throws Exception {
		Boolean msgAprobacionBaseDatos = false;
		String nomProceso = SettingsRun.getTestData().getParameter("Nombre del Proceso").trim();
		String TipoApro = SettingsRun.getTestData().getParameter("Aprobar/Eliminar").trim();
		WebElement tablaResultados = this.findElement(TablaReferenciasR);
		List<WebElement> filas = tablaResultados.findElements(By.tagName("tr"));
		List<String> textosObtenidos = new ArrayList<>();
		String informacionTabla = "";
		List<WebElement> celdas = null;
		WebElement checkboxsl = null;
		// se extrae la información de la tabla
		String xpathRemplazarProceso = "";
//	    for (int i=1; i<filas.size();i++) {
//	        WebElement fila = filas.get(i);
//	        informacionTabla = fila.getText();
//	        textosObtenidos.add(informacionTabla);
		xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
		checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
//		Selecciona el proceso de la BD
		if (this.element(checkboxsl) != null) {
			this.click(checkboxsl);
			Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
			Evidence.save("Proceso de BD selecionado");
			msgAprobacionBaseDatos = true;
		} else if (this.element(checkboxsl) == null) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
			msgAprobacionBaseDatos = false;
			Mensajes = "Preceso de referencia no encontrado";
			return msgAprobacionBaseDatos;
		}
		DXCUtil.wait(1);
//		Escenario Donde Se aprueba el proceso de las Referencias Agregadas por Archivo
		if (TipoApro.contains("Aprobar")) {
			this.click(AprobarRef);
			WebElement msgAlert = this.findElement(msgAlerta);
			if (this.element(msgAlert) != null) {
				Evidence.save("Base de datos Recibida exitosamente");
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido el proceso");
				Mensajes = this.element(msgAlert).getText();
			} else if (this.element(msgAlert) == null) {
				Evidence.save("Base de datos NO Recibida");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
				Mensajes = "Ah ocurrido un error al aprobar el proceso";
				msgAprobacionBaseDatos = false;
			}
//		Escenario Donde Se Elimina el proceso de las Referencias Agregadas por Archivo
		} else if (TipoApro.contains("Eliminar")) {
			this.click(eliminar);
			DXCUtil.wait(1);
			Evidence.save("Mensaje de Eliminar");
			this.acceptDialog();
			if (this.element(msgAlerta) == null) {
				Evidence.save("Base de datos Eliminada exitosamente");
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Eliminado el proceso");
			} else if (this.element(msgAlerta) != null) {
				WebElement msgAlert = this.findElement(msgAlerta);
				Evidence.save("Base de datos NO eliminada");
				Mensajes = this.element(msgAlert).getText();
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha eliminado el proceso");
				Mensajes = "Ah ocurrido un error al eliminar el proceso";
				msgAprobacionBaseDatos = false;
			}
		}
		//Validacion si el campo en la hoja de Datos se encuentra Vacio
		if (TipoApro == null || TipoApro.equals("")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No Se seleciono un tipo de prueba para la Base de datos");
			Mensajes = "Revise la columna (Aprobar/Eliminar) en su hoja de datos";
			msgAprobacionBaseDatos = false;
		}

		return msgAprobacionBaseDatos;
	}
// Escenario en donde se hace la consulta de los Procesos con sus diferentes tipos de Filtros
	public boolean ConsultaProcesos() throws Exception {
		this.pageOrigen = new PageOrigen(this);
		Boolean msgConsultaProcesos = false;
		String Convenio = SettingsRun.getTestData().getParameter("Codigo Convenio Empresa").trim();
		String nomProceso = SettingsRun.getTestData().getParameter("Nombre del Proceso").trim();
		String numProceso = SettingsRun.getTestData().getParameter("Numero del Proceso").trim();
		String tipoConsulta = SettingsRun.getTestData().getParameter("Tipo Consulta").trim();
		String Detalle = SettingsRun.getTestData().getParameter("Ver el detalle del proceso").trim();

		DXCUtil.wait(2);
		selectListContainsItems(ConveniodeRecaudo, Convenio);
		//Tipo de consulta del Proceso con su numero
		if (tipoConsulta.contains("Numero Proceso")) {
			this.write(numeroproceso, numProceso);
			Evidence.save("Consulta de proceso con " + tipoConsulta);
			DXCUtil.wait(1);
			this.click(Mostrardatos);
			DXCUtil.wait(1);
			String msgerror = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			// WebElement msgAlert = this.findElement(msgAlerta);
			WebElement TablaConsultaPro = null;
			if (msgerror == null) {
				TablaConsultaPro = this.findElement(tablaConsultaProcesos);
				List<WebElement> Informacion = TablaConsultaPro.findElements(By.tagName(("tr")));
				datos = new String[Informacion.size()];
				for (int i = 0; i < datos.length; i++) {
					datos[i] = Informacion.get(i).getText();
					Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
				}
				Mensajes = "Se ha traido la informacion de la referencia Correctamente";
				Evidence.save("Consulta Exitosa");
				msgConsultaProcesos = true;

			} else {
				Evidence.save("No se encontraron Datos");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha encontrado el proceso");
				Mensajes = msgerror;
				msgConsultaProcesos = false;

			}
			//Selecion donde se le indica si quiere ver el detalle de este Proceso
			if (Detalle.contains("SI")) {
				WebElement Detalleconsulta = null;
				String xpathRemplazarNumProceso = "";
				xpathRemplazarNumProceso = NumProceso.replace("NumProceso", numProceso);
				Detalleconsulta = this.element(By.xpath(xpathRemplazarNumProceso));
				if (this.element(Detalleconsulta) != null) {
					DXCUtil.wait(1);
					this.click(Detalleconsulta);
					DXCUtil.wait(2);
					Evidence.save("Consulta Exitosa");
					Reporter.reportEvent(Reporter.MIC_INFO, "Redireccion Proceso");
				} else if (this.element(Detalleconsulta) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Redireccion Proceso no encontrada");
					msgConsultaProcesos = false;
					Mensajes = "Preceso de referencia no encontrado";
					return msgConsultaProcesos;
				}

			}
			//Selecion donde se le indica si NO quiere ver el detalle de este Proceso
			if (Detalle.contains("NO")) {
				Reporter.reportEvent(Reporter.MIC_INFO, "Consulta Terminada");
				msgConsultaProcesos = true;
			}
			//Validacion si el campo en la hoja de Datos se encuentra Vacio
			if (Detalle == null || Detalle.equals("")) {
				Mensajes = "Seleccione si ver el detalle o no";
				msgConsultaProcesos = false;

			}
		}
		//Tipo de consulta del Proceso con su Nombre
		if (tipoConsulta.contains("Nombre Proceso")) {
			this.write(nombreproceso, nomProceso);
			Evidence.save("Consulta de proceso con " + tipoConsulta);
			DXCUtil.wait(1);
			this.click(Mostrardatos);
			DXCUtil.wait(1);
			String msgerror = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			// WebElement msgAlert = this.findElement(msgAlerta);
			WebElement TablaConsultaPro = this.element(tablaConsultaProcesos);
			if (msgerror == null) {
				Mensajes = "Se ha traido la informacion del proceso Correctamente";
				Evidence.save("Consulta Exitosa");
				msgConsultaProcesos = true;

			} else if (msgerror != null) {
				Evidence.save("No se encontraron Datos");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha encontrado el proceso");
				Mensajes = msgerror;
				msgConsultaProcesos = false;
				return msgConsultaProcesos;

			}
			// Condicion donde se repite hasta que no Este o se deshabilitado el elemento seleccionado
			do {

				DXCUtil.wait(2);
				TablaConsultaPro = this.findElement(tablaConsultaProcesos);
				Informacion = TablaConsultaPro.findElements(By.tagName(("tr")));
				datos = new String[Informacion.size()];
				for (int i = 0; i < datos.length; i++) {
					datos[i] = Informacion.get(i).getText();
					Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
				}
				//Selecion donde se le indica si quiere ver el detalle de estos Procesos
				if (Detalle.contains("SI")) {
					List<WebElement> NumerosPro = this.findElements(NumerosProcesos);
					String[] nOprocesos = new String[NumerosPro.size()];
					for (int i = 0; i < NumerosPro.size(); i++) {
						nOprocesos[i] = NumerosPro.get(i).getText();
					}
					for (String elemento : nOprocesos) {
						DXCUtil.wait(3);
						WebElement Detalleconsulta = null;
						String xpathRemplazarNumProceso = "";
						xpathRemplazarNumProceso = NumProceso.replace("NumProceso", elemento);
						Detalleconsulta = this.element(By.xpath(xpathRemplazarNumProceso));
						this.click(Detalleconsulta);
						DXCUtil.wait(2);
						Evidence.save("Consulta Exitosa" + elemento);
						DXCUtil.wait(3);
						this.click(regresar);
						DXCUtil.wait(3);
						Evidence.save("Consulta Exitosa");
					}
				}
				//Selecion donde se le indica si NO quiere ver el detalle de estos Procesos
				if (Detalle.contains("NO")) {
					Evidence.save("Consulta Sin detalle");
					Reporter.reportEvent(Reporter.MIC_INFO, "Consulta Correcta");
					msgConsultaProcesos = true;
				}
				if (this.element(btnSiguiente) != null
						&& !this.element(btnSiguiente).getAttribute("class").contains("aspNetDisabled")) {
					this.click(btnSiguiente);
				}
				Reporter.reportEvent(Reporter.MIC_INFO, "Redireccion de procesos exitosa");

			} while (this.element(btnSiguiente) != null
					&& !this.element(btnSiguiente).getAttribute("class").contains("aspNetDisabled"));
			//Validacion si el campo en la hoja de Datos se encuentra Vacio
			if (Detalle == null || Detalle.equals("")) {
				Mensajes = "Seleccione si ver el detalle o no";
				msgConsultaProcesos = false;

			}

		}
		//Tipo de consulta del Proceso con su Estado
		if (tipoConsulta.contains("Estado Proceso")) {
			String Estado = SettingsRun.getTestData().getParameter("Estados").trim();
			if (Estado.contains("Actualizado Parcial")) {
				selectListContainsItems(btnEstados, "Actualizado Parcial");
			}
			if (Estado.contains("Recaudo Rechazado")) {
				selectListContainsItems(btnEstados, "Recaudo Rechazado");
			}
			if (Estado.contains("Pendiente de Actualizar")) {
				selectListContainsItems(btnEstados, "Pendiente de Actualizar");
			}
			if (Estado.contains("Actualizacion Exitosa")) {
				selectListContainsItems(btnEstados, "Actualizacion Exitosa");
			}
			if (Estado == null || Estado.equals("")) {
				Mensajes = "Seleccione un estado del proceso para realizar la consulta";
				msgConsultaProcesos = false;
			}
			DXCUtil.wait(2);
			Evidence.save("Seleccion de estado de proceso (" + Estado + ")");
			this.click(Mostrardatos);

			DXCUtil.wait(1);
			String msgerror = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			WebElement TablaConsultaPro = null;

			if (msgerror == null) {
				Mensajes = "Se ha traido la informacion del proceso Correctamente";
				Evidence.save("Consulta Exitosa");
				msgConsultaProcesos = true;

			} else if (msgerror != null) {
				Evidence.save("No se encontraron Datos");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha encontrado el proceso");
				Mensajes = msgerror;
				msgConsultaProcesos = false;
				return msgConsultaProcesos;

			}
			// Condicion donde se repite hasta que no Este o se deshabilitado el elemento seleccionado
			do {

				DXCUtil.wait(2);
				TablaConsultaPro = this.findElement(tablaConsultaProcesos);
				Informacion = TablaConsultaPro.findElements(By.tagName(("tr")));
				datos = new String[Informacion.size()];
				for (int i = 0; i < datos.length; i++) {
					datos[i] = Informacion.get(i).getText();
					Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
				}
				//Selecion donde se le indica si quiere ver el detalle de estos Procesos
				if (Detalle.contains("SI")) {
					List<WebElement> NumerosPro = this.findElements(NumerosProcesos);
					String[] nOprocesos = new String[NumerosPro.size()];
					for (int i = 0; i < NumerosPro.size(); i++) {
						nOprocesos[i] = NumerosPro.get(i).getText();
					}
					for (String elemento : nOprocesos) {
						DXCUtil.wait(3);
						WebElement Detalleconsulta = null;
						String xpathRemplazarNumProceso = "";
						xpathRemplazarNumProceso = NumProceso.replace("NumProceso", elemento);
						Detalleconsulta = this.element(By.xpath(xpathRemplazarNumProceso));
						this.click(Detalleconsulta);
						DXCUtil.wait(2);
						Evidence.save("Consulta Exitosa" + elemento);
						DXCUtil.wait(3);
						this.click(regresar);
						DXCUtil.wait(3);
						Evidence.save("Consulta Exitosa");
					}
				}
				//Selecion donde se le indica si NO quiere ver el detalle de estos Procesos
				if (Detalle.contains("NO")) {
					Evidence.save("Consulta Sin detalle");
					Reporter.reportEvent(Reporter.MIC_INFO, "Consulta Correcta");
					msgConsultaProcesos = true;
				}
				if (this.element(btnSiguiente) != null
						&& !this.element(btnSiguiente).getAttribute("class").contains("aspNetDisabled")) {
					this.click(btnSiguiente);
				}
				Reporter.reportEvent(Reporter.MIC_INFO, "Redireccion de procesos exitosa");

			} while (this.element(btnSiguiente) != null
					&& !this.element(btnSiguiente).getAttribute("class").contains("aspNetDisabled"));
			//Validacion si el campo en la hoja de Datos se encuentra Vacio
			if (Detalle == null || Detalle.equals("")) {
				Mensajes = "Seleccione si ver el detalle o no";
				msgConsultaProcesos = false;

			}
		}
		//Tipo de consulta del Proceso con su Estado
		if (tipoConsulta.contains("Fecha Proceso")) {
			String Fechadesde = SettingsRun.getTestData().getParameter("Fecha Desde(DD/MM/AA)").trim();
			String Fechahasta = SettingsRun.getTestData().getParameter("Fecha Hasta(DD/MM/AA)").trim();
			this.write(fechaDesde, Fechadesde);
			this.write(fechaHasta, Fechahasta);
			Evidence.save("Consulta de proceso con " + tipoConsulta);
			this.click(Mostrardatos);
			DXCUtil.wait(1);
			String msgerror = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			WebElement TablaConsultaPro = null;
			if (msgerror == null) {
				Mensajes = "Se ha traido la informacion del proceso Correctamente";
				Evidence.save("Consulta Exitosa");
				msgConsultaProcesos = true;

			} else if (msgerror != null) {
				Evidence.save("No se encontraron Datos");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha encontrado el proceso");
				Mensajes = msgerror;
				msgConsultaProcesos = false;
				return msgConsultaProcesos;

			}
			// Condicion donde se repite hasta que no Este o se deshabilitado el elemento seleccionado
			do {

				DXCUtil.wait(2);
				TablaConsultaPro = this.findElement(tablaConsultaProcesos);
				Informacion = TablaConsultaPro.findElements(By.tagName(("tr")));
				datos = new String[Informacion.size()];
				for (int i = 0; i < datos.length; i++) {
					datos[i] = Informacion.get(i).getText();
					Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
				}
				//Selecion donde se le indica si quiere ver el detalle de estos Procesos
				if (Detalle.contains("SI")) {
					List<WebElement> NumerosPro = this.findElements(NumerosProcesos);
					String[] nOprocesos = new String[NumerosPro.size()];
					for (int i = 0; i < NumerosPro.size(); i++) {
						nOprocesos[i] = NumerosPro.get(i).getText();
					}
					for (String elemento : nOprocesos) {
						DXCUtil.wait(3);
						WebElement Detalleconsulta = null;
						String xpathRemplazarNumProceso = "";
						xpathRemplazarNumProceso = NumProceso.replace("NumProceso", elemento);
						Detalleconsulta = this.element(By.xpath(xpathRemplazarNumProceso));
						this.click(Detalleconsulta);
						DXCUtil.wait(2);
						Evidence.save("Consulta Exitosa" + elemento);
						DXCUtil.wait(3);
						this.click(regresar);
						DXCUtil.wait(3);
						Evidence.save("Consulta Exitosa");
					}
				}
				//Selecion donde se le indica si NO quiere ver el detalle de estos Procesos
				if (Detalle.contains("NO")) {
					Evidence.save("Consulta Sin detalle");
					Reporter.reportEvent(Reporter.MIC_INFO, "Consulta Correcta");
					msgConsultaProcesos = true;
				}
				if (this.element(btnSiguiente) != null
						&& !this.element(btnSiguiente).getAttribute("class").contains("aspNetDisabled")) {
					this.click(btnSiguiente);
				}
				Reporter.reportEvent(Reporter.MIC_INFO, "Redireccion de procesos exitosa");

			} while (this.element(btnSiguiente) != null
					&& !this.element(btnSiguiente).getAttribute("class").contains("aspNetDisabled"));
			//Validacion si el campo en la hoja de Datos se encuentra Vacio
			if (Detalle == null || Detalle.equals("")) {
				Mensajes = "Seleccione si ver el detalle o no";
				msgConsultaProcesos = false;

			}
		}
		//Validacion si el campo en la hoja de Datos se encuentra Vacio
		if (tipoConsulta == null || tipoConsulta.equals("")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No selecciono  un tipo de consulta");
			Mensajes = "Seleccione un tipo de Consulta de procesos en la columna(Tipo Consulta) de su hoja de Datos";
			msgConsultaProcesos = false;
		}

		return msgConsultaProcesos;
	}
	//Escenario donde se Agregan,Eliminan, Modificacion de Valor de pago y Fecha de las rerferencias de la Empresa
	public boolean NovedadesBD() throws Exception {
		//Datos a Extraer
		Boolean msgNovedad = false;
		String tipoNovedad = SettingsRun.getTestData().getParameter("Tipo Novedad").trim();
		String Convenio = SettingsRun.getTestData().getParameter("Codigo Convenio Empresa").trim();
		String nomProceso = SettingsRun.getTestData().getParameter("Nombre Proceso(Modificacion)").trim();
		String refrenciaModificada = SettingsRun.getTestData().getParameter("Referencia 1 (Adicionar/Eliminar/Modificada)").trim();
		String fechaModificacion = SettingsRun.getTestData().getParameter("Fecha Modificacion(DD/MM/AA)").trim();
		String valorModificacion = SettingsRun.getTestData().getParameter("Valor a Pagar Modificacion").trim();
		String ReferenciaMod1 = SettingsRun.getTestData().getParameter("Referencia 1 (Adicionar/Eliminar/Modificada)").trim();
		String ReferenciaMod2 = SettingsRun.getTestData().getParameter("Referencia2 (Adicionar/Eliminar)").trim();
		String numeroReferencias = SettingsRun.getTestData().getParameter("Numero de Referencias (Eliminar/Adicionar)").trim();
		String tipoAdicionRef = SettingsRun.getTestData().getParameter("Tipo Adicion Referencias").trim();
		String ArchivoCarga = SettingsRun.getTestData().getParameter("Estructura").trim();
		String ArchivoRuta = SettingsRun.getTestData().getParameter("Ruta Archivo Referencias").trim();

		DXCUtil.wait(1);
		selectListItem(ConveniodeRecaudo, Convenio);
		//Escenario donde se realizla modificacion Masiva en la fecha de las referencias en el portal
		if (tipoNovedad.contains("Cambio Fecha")) {
			this.click(CambioFecha);
			DXCUtil.wait(5);
			this.write(fechaNueva, fechaModificacion);
			Evidence.save("Seleccion de novedad con " + tipoNovedad);
			this.click(btnCambioFecha);
			String msgerror = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			Evidence.save("El resultado de la moficacion es: " + msgerror);
			if (msgerror.contains("Modificación exitosa")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Modificado las Referencias");
				Mensajes = msgerror;
				msgNovedad = true;
				this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
				selectListContainsItems(ConveniodeRecaudo, Convenio);
				this.write(Referencia1, refrenciaModificada);
				Evidence.save("Datos de la referencia a consultar");
				this.click(Mostrardatos);
				DXCUtil.wait(2);
				WebElement msgAlert = this.element(msgAlerta);
				if (msgAlert == null) {
					Evidence.save("Refererncia Consultada");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Consultado la referencia correctamente");
					WebElement TablaDatosRef = this.element(tablaReferencias);
					List<WebElement> Informacion = TablaDatosRef.findElements(By.tagName(("tr")));
					datos = new String[Informacion.size()];
					for (int i = 0; i < datos.length; i++) {
						datos[i] = Informacion.get(i).getText();
					}
					for (int i = 0; i < datos.length; i++) {
						Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
					}
					Mensajes = "Se ha traido la informacion de la referencia Correctamente";

					if (datos[3].contains(convertirFecha(fechaModificacion))) {
						Reporter.reportEvent(Reporter.MIC_PASS, "La fecha coincide con la modificacion");
						msgNovedad = true;
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL, "La fecha NO coincide con la modificacion");

						msgNovedad = false;
					}
					Mensajes = "La modificacion fue exitosa";
				} else if (msgAlert != null) {
					Mensajes = this.element(msgAlert).getText();
					if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
						Evidence.save("Refererncia No Existe");
						msgNovedad = false;
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"No se ha encontrado la Referencia Y No podemos validar la modificacion");
					}
				}

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "NO Se pudo Modificar las Referencias");
				Mensajes = msgerror;
				msgNovedad = false;

			}
		}
		//Escenario donde se realizla modificacion Masiva en el valor de las referencias en el portal
		if (tipoNovedad.contains("Cambio Valor")) {
			this.click(CambioValor);
			DXCUtil.wait(3);
			this.write(valorNuevo, valorModificacion);
			Evidence.save("Seleccion de novedad con " + tipoNovedad);
			this.click(CambioValor);
			DXCUtil.wait(3);
			this.mouseOver(btnCambioValor);
			DXCUtil.wait(2);
			this.mouseClick();
			String msgerror = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			Evidence.save("El resultado de la moficacion es: " + msgerror);
			if (msgerror.contains("Modificación exitosa")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Modificado las Referencias");
				Mensajes = msgerror;
				msgNovedad = true;
				this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
				selectListContainsItems(ConveniodeRecaudo, Convenio);
				this.write(Referencia1, refrenciaModificada);
				Evidence.save("Datos de la referencia a consultar");
				this.click(Mostrardatos);
				DXCUtil.wait(2);
				WebElement msgAlert = this.element(msgAlerta);
				if (msgAlert == null) {
					Evidence.save("Refererncia Consultada");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Consultado la referencia correctamente");
					WebElement TablaDatosRef = this.element(tablaReferencias);
					List<WebElement> Informacion = TablaDatosRef.findElements(By.tagName(("tr")));
					datos = new String[Informacion.size()];
					for (int i = 0; i < datos.length; i++) {
						datos[i] = Informacion.get(i).getText();
					}
					for (int i = 0; i < datos.length; i++) {
						Reporter.reportEvent(Reporter.MIC_INFO, datos[i]);
					}
					Mensajes = "Se ha traido la informacion de la referencia Correctamente";
					if (datos[2].contains(DXCUtil.formatCurrency(DXCUtil.toDouble(valorModificacion, 0), 2))) {
						Reporter.reportEvent(Reporter.MIC_PASS,
								"El valor de la referencia coincide con la modificacion");
						msgNovedad = true;
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"El valor de la referencia NO coincide con la modificacion");

						msgNovedad = false;
					}
					Mensajes = "La modificacion fue exitosa";
				} else if (msgAlert != null) {
					Mensajes = this.element(msgAlert).getText();
					if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
						Evidence.save("Refererncia No Existe");
						msgNovedad = false;
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"No se ha encontrado la Referencia Y No podemos validar la modificacion");
					}
				}

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "NO Se pudo Modificar las Referencias");
				Mensajes = msgerror;
				msgNovedad = false;

			}
		}
		//Escenario donde se Eliminan Refrencias Ya creadas en el portal
		if (tipoNovedad.contains("Eliminar Referencias")) {
			//Se realiza la consulta de las referencias para comprobar su existencia
			this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
			selectListContainsItems(ConveniodeRecaudo, Convenio);
			this.write(Referencia1, ReferenciaMod1);
			if (numeroReferencias.contains("2")) {
				this.write(Referencia2, ReferenciaMod2);
			}
			Evidence.save("Datos de la referencia a consultar");
			this.click(Mostrardatos);
			DXCUtil.wait(2);
			WebElement msgAlert = this.element(msgAlerta);
			if (msgAlert == null) {
				Evidence.save("Refererncia Consultada");
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha Consultado la referencia correctamente");
				msgNovedad = true;
			} else if (msgAlert != null) {
				Mensajes = this.element(msgAlert).getText();
				if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
					Evidence.save("Refererncia No Existe");
					msgNovedad = false;
					Reporter.reportEvent(Reporter.MIC_FAIL,
							"No se ha encontrado la Referencia Antes de que se inciara el proceso de eliminacion");
					return msgNovedad;
				}
			}
			this.pageOrigen.irAOpcion(null, "Recaudo", "Novedades Base de Datos");
			this.click(eliminarAdicionar);
			DXCUtil.wait(4);
			selectListItem(tipoNovedadRef, "Eliminar");
			DXCUtil.wait(2);
			this.write(NombreProceso, nomProceso);
			Evidence.save("Seleccion de novedad con " + tipoNovedad);
			this.write(Referencia1, ReferenciaMod1);
			if (numeroReferencias.contains("2")) {
				this.write(Referencia2, ReferenciaMod2);
			}
			Evidence.save("Infromacion a Eliminar");
			this.click(agregarReferencia);
			int cont = 0;
			do {
				DXCUtil.wait(1);
				cont++;
			} while (this.element(TablarefArch) == null && cont <= 2);
			if (this.element(msgAlerta) != null) {
				Evidence.save("error Cargue");
				Mensajes = this.getText(msgAlerta);
				return msgNovedad = false;
			}
			this.click(continuarRef);
			Evidence.save("Confirmacion");
			DXCUtil.wait(2);
			WebElement checkboxsl = null;
			String xpathRemplazarProceso = "";
			xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
			checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
//			Selecion del Proceso para su Aprobacion
			if (this.element(checkboxsl) != null) {
				this.click(checkboxsl);
				Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
				Evidence.save("Proceso de BD selecionado");
				msgNovedad = true;
			} else if (this.element(checkboxsl) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
				msgNovedad = false;
				Mensajes = "Preceso de referencia no encontrado";
				return msgNovedad;
			}
			DXCUtil.wait(1);

			this.click(AprobarRef);

			msgAlert = this.findElement(msgAlerta);
			if (this.element(msgAlert) != null) {
				Evidence.save("Base de datos Recibida exitosamente");
				Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido el proceso");
			} else if (this.element(msgAlert) == null) {
				Evidence.save("Base de datos NO Recibida");
				Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
				Mensajes = "Ah ocurrido un error al aprobar el proceso";
				msgNovedad = false;

			}
			//Se realiza la consulta de las referencias para comprobar su Eliminacion Exitosa
			this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
			selectListContainsItems(ConveniodeRecaudo, Convenio);
			this.write(Referencia1, ReferenciaMod1);
			if (numeroReferencias.contains("2")) {
				this.write(Referencia2, ReferenciaMod2);
			}
			Evidence.save("Datos de la referencia a consultar");
			this.click(Mostrardatos);
			DXCUtil.wait(2);
			msgAlert = this.element(msgAlerta);
			if (msgAlert != null) {
				Mensajes = this.element(msgAlert).getText();
				if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
					Evidence.save("Refererncia Ya No Existe");
					msgNovedad = true;
					Reporter.reportEvent(Reporter.MIC_PASS,
							"No se ha encontrado la Referencia despues del proceso de eliminacion");
					return msgNovedad;
				}
			}

			if (msgAlert == null) {
				Evidence.save("Refererncia Consultada");
				Reporter.reportEvent(Reporter.MIC_FAIL,
						"Se ha Consultado la referencia y existe despues de su eliminacion, revise si existen dos referencias iguales");
				msgNovedad = false;
			}

		}
		//Escenario donde se Agregan Refrencias en el portal
		if (tipoNovedad.contains("Adicionar Referencias")) {
			//Escenario donde se Agregan Refrencias De manera Digitada
			if (tipoAdicionRef.contains("Digitado")) {
				//Se consulta el numero de referencia a Agregar para comprobar que no exista en el portal
				this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
				selectListContainsItems(ConveniodeRecaudo, Convenio);
				this.write(Referencia1, ReferenciaMod1);
				if (numeroReferencias.contains("2")) {
					this.write(Referencia2, ReferenciaMod2);
				}
				Evidence.save("Datos de la referencia a consultar");
				this.click(Mostrardatos);
				DXCUtil.wait(2);
				WebElement msgAlert = this.element(msgAlerta);
				if (msgAlert == null) {
					Evidence.save("Refererncia Consultada");
					Reporter.reportEvent(Reporter.MIC_FAIL,
							"Se ha Consultado la referencia correctamente y No deberia Existir esta");
					msgNovedad = false;
					return msgNovedad;
				} else if (msgAlert != null) {
					Mensajes = this.element(msgAlert).getText();
					if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
						Evidence.save("Refererncia No Existe");
						msgNovedad = true;
						Reporter.reportEvent(Reporter.MIC_PASS,
								"No se ha encontrado la Referencia Antes de que se inciara el proceso de Adicion");
					}
				}
				this.pageOrigen.irAOpcion(null, "Recaudo", "Novedades Base de Datos");
				this.click(eliminarAdicionar);
				DXCUtil.wait(4);
				selectListItem(tipoNovedadRef, "Adicionar");
				DXCUtil.wait(2);
				this.write(NombreProceso, nomProceso);
				Evidence.save("Seleccion de novedad con " + tipoNovedad);
				this.write(Referencia1, ReferenciaMod1);
				if (numeroReferencias.contains("2")) {
					this.write(Referencia2, ReferenciaMod2);
				}
				this.write(ValorPago, valorModificacion);
				this.write(fechaLimite, fechaModificacion);
				Evidence.save("Infromacion a Adicionar");
				this.click(agregarReferencia);
				int cont = 0;
				do {
					DXCUtil.wait(1);
					cont++;
				} while (this.element(TablarefArch) == null && cont <= 2);
				if (this.element(msgAlerta) != null) {
					Evidence.save("error Cargue");
					Mensajes = this.getText(msgAlerta);
					return msgNovedad = false;
				}
				this.click(continuarRef);
				DXCUtil.wait(2);
				WebElement checkboxsl = null;
				String xpathRemplazarProceso = "";
				xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
				checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
//				Selecion del Proceso para su Aprobacion
				if (this.element(checkboxsl) != null) {
					this.click(checkboxsl);
					Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
					Evidence.save("Proceso de BD selecionado");
					msgNovedad = true;
				} else if (this.element(checkboxsl) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
					msgNovedad = false;
					Mensajes = "Preceso de referencia no encontrado";
					return msgNovedad;
				}
				DXCUtil.wait(1);

				this.click(AprobarRef);

				msgAlert = this.findElement(msgAlerta);
				if (this.element(msgAlert) != null) {
					Evidence.save("Base de datos Recibida exitosamente");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido el proceso");
				} else if (this.element(msgAlert) == null) {
					Evidence.save("Base de datos NO Recibida");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
					Mensajes = "Ah ocurrido un error al aprobar el proceso";
					msgNovedad = false;

				}
				//Se vuelve a consultar para comprobar que se agrego la referencia correctamente al portal
				this.pageOrigen.irAOpcion(null, "Recaudo", "Consulta de Referencias");
				selectListContainsItems(ConveniodeRecaudo, Convenio);
				this.write(Referencia1, ReferenciaMod1);
				if (numeroReferencias.contains("2")) {
					this.write(Referencia2, ReferenciaMod2);
				}
				Evidence.save("Datos de la referencia a consultar");
				this.click(Mostrardatos);
				DXCUtil.wait(2);
				msgAlert = this.element(msgAlerta);
				if (msgAlert != null) {
					Mensajes = this.element(msgAlert).getText();
					if (Mensajes.contains("REFERENCIA NO EXISTE 00")) {
						Evidence.save("Refererncia No Existe");
						msgNovedad = false;
						Reporter.reportEvent(Reporter.MIC_FAIL,
								"No se ha encontrado la Referencia despues del proceso de Adicion");
						return msgNovedad;
					}
				}

				if (msgAlert == null) {
					Evidence.save("Refererncia Consultada");
					Reporter.reportEvent(Reporter.MIC_PASS,
							"Se ha Consultado la referencia y existe despues de su Adicion");
					msgNovedad = true;
				}

			}
			//Escenario donde se Agregan Referencias con Cargue de Archivo
			if (tipoAdicionRef.contains("Archivo")) {
				this.controllerDestinosMasivos = new ControllerDestinosMasivos(null);
				this.click(eliminarAdicionar);
				DXCUtil.wait(4);
				selectListItem(tipoNovedadRef, "Adicionar");
				DXCUtil.wait(2);
				this.write(NombreProceso, nomProceso);
				Evidence.save("Seleccion de novedad con " + tipoNovedad);
				//Tipo de Archivo 1
				if (ArchivoCarga.contains("Excel 1 Referencia")) {
					Boolean CreacionArchivo = this.controllerDestinosMasivos.ReferenciasEcxel1(ArchivoRuta);
					if (CreacionArchivo) {
						Mensajes = "Se creo el Archivo correctamente";
						msgNovedad = true;

					} else {
						Mensajes = "Hubo un error en la creacion del archivo";
						msgNovedad = false;
						return msgNovedad;
					}
				}
				//Tipo de Archivo 2
				if (ArchivoCarga.contains("Excel 2 Referencia")) {
					Boolean CreacionArchivo = this.controllerDestinosMasivos.ReferenciasEcxel2(ArchivoRuta);
					if (CreacionArchivo) {
						Mensajes = "Se creo el Archivo correctamente";
						msgNovedad = true;
					} else {
						Mensajes = "Hubo un error en la creacion del archivo";
						msgNovedad = false;
						return msgNovedad;
					}
				}
				//Tipo de Archivo 3
				if (ArchivoCarga.contains("Archivo 130 Posiciones") || ArchivoCarga.contains("Archivo 50 Posiciones")) {
					Boolean CreacionArchivo = this.controllerDestinosMasivos.ReferenciasArchivoPlano130_50(ArchivoRuta);
					if (CreacionArchivo) {
						Mensajes = "Se creo el Archivo correctamente";
						msgNovedad = true;
					} else {
						Mensajes = "Hubo un error en la creacion del archivo";
						msgNovedad = false;
						return msgNovedad;
					}

				}
				this.mouseOver(BtnCargaArchivoNov);
				this.mouseClick();
				Evidence.save("Cargue de archivo");
				DXCUtil.wait(2);
				String rutaArchiv = SettingsRun.getTestData().getParameter("rutaArchivo");
				DXCUtil.cargueArchivo(rutaArchiv);
				DXCUtil.wait(2);
				Evidence.save("Cargue Archivo");
				int cont = 0;
				do {
					DXCUtil.wait(1);
					cont++;
				} while (this.element(TablarefArch) == null && cont <= 2);
				if (this.element(msgAlerta) != null) {
					Evidence.save("error Cargue");
					Mensajes = this.getText(msgAlerta);
					return msgNovedad = false;
				}
				Evidence.save("Archivo ya Cargado");
				Mensajes = "Se ha cargado el archivo correctamente";
				DXCUtil.wait(2);
				this.click(continuarRef);
				Reporter.reportEvent(Reporter.MIC_PASS, "Se cargo el archivo correctamente en la novedad");
				DXCUtil.wait(2);
				Evidence.save("Bases de Datos Por aprobar");
				WebElement checkboxsl = null;
				String xpathRemplazarProceso = "";
				xpathRemplazarProceso = Checkbox.replace("NombreDelProceso/Numero", nomProceso);
				checkboxsl = this.element(By.xpath(xpathRemplazarProceso));
				//Se selecciona el Proceso de la BD para su Aprobacion
				if (this.element(checkboxsl) != null) {
					this.click(checkboxsl);
					Reporter.reportEvent(Reporter.MIC_INFO, "Proceso de BD selecionado");
					Evidence.save("Proceso de BD selecionado");
					msgNovedad = true;
				} else if (this.element(checkboxsl) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Proceso de BD no encontrado");
					msgNovedad = false;
					Mensajes = "Preceso de referencia no encontrado";
				}
				DXCUtil.wait(1);
				this.click(AprobarRef);
				WebElement msgAlert = this.findElement(msgAlerta);
				if (this.element(msgAlert) != null) {
					Evidence.save("Base de datos Recibida exitosamente");
					Reporter.reportEvent(Reporter.MIC_PASS, "Se ha añadido el proceso");
					Mensajes = this.element(msgAlert).getText();
				} else if (this.element(msgAlert) == null) {
					Evidence.save("Base de datos NO Recibida");
					Reporter.reportEvent(Reporter.MIC_FAIL, "No Se ha añadido el proceso");
					Mensajes = "Ah ocurrido un error al aprobar el proceso";
					msgNovedad = false;
				}

			}

		}
		//Validacion si el campo en la hoja de Datos se encuentra Vacio
		if (tipoNovedad == null || tipoNovedad.equals("")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No selecciono  un tipo de Novedad");
			Mensajes = "Seleccione un tipo de Novedad de BD en la columna(Tipo Novedad) de su hoja de Datos";
			msgNovedad = false;
		}

		return msgNovedad;
	}
	//Metodo donde convierte el Dato de entrada con otro formato seleccionado
	public static String convertirFecha(String fechaEntrada) {
		SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/mm/yyyy");
		SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-mm-dd");
		String fechaSalida = "";
		try {
			Date fecha = formatoEntrada.parse(fechaEntrada);
			fechaSalida = formatoSalida.format(fecha);
		} catch (ParseException e) {
			e.printStackTrace(); // Maneja la excepción en caso de error en el formato de fecha
		}
		return fechaSalida;
	}
	//Capturador de Mensajes
	public String getMensajes() {
		// TODO Auto-generated method stub
		return Mensajes;
	}

	public static String[] getInformacionRef() {

		return datos;
	}

}
