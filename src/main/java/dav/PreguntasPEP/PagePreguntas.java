package dav.PreguntasPEP;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PagePreguntas extends BasePageWeb {
	PageOrigen pageOrigen = null;

	// =======================================================================================================================
	// ===== LOCATORS DE LA PARAMETROS GENERALES (Orden por secuencia de uso)
	// TITULOS
	By tituloPrincipal = By.xpath("//h2[text()='Información PEP']");

	// PREGUNTA 1
	String inpPoliticamenteExpuesto = "//div[contains(text(),'persona expuesta políticamente')]/following-sibling::div/label[{SELECCION}]";
	// PREGUNTA 2
	String inpCargosPublicos = "//*[@id='cphCuerpo_DivPreg2']/following-sibling::div/label[{SELECCION}]";
	// PREGUNTA 3
	String inpEstaExpuesto = "//*[@id='pnlSiPreguntaDos']/div[1]//b[contains(text(),'Está expuesto')]/parent::div/following-sibling::div[1]/label[{SELECCION}]";
	By slcCargoPublico = By.cssSelector("#cphCuerpo_ddlCargoPublico");
	By inpFechaDesde = By.cssSelector("#cphCuerpo_FechaVinculacion");
	By inpFechaHasta = By.cssSelector("#cphCuerpo_FechaDesvinculacion");
	By spanAcutalVinculado = By.xpath("//label/*[contains(text(),'Actualmente')]/following-sibling::span");
	// PREGUNTA 4
	String inpEsFamiliar = "//*[@id='pnlSiPreguntaDos']/div[2]//b[contains(text(),'Es usted familiar')]/parent::div/following-sibling::div[1]/label[{SELECCION}]";
	By slcParentesco = By.cssSelector("#cphCuerpo_ddlParentesco");
	By inpPrimerNombre = By.cssSelector("#cphCuerpo_txtNombreFamiliar");
	By inpPrimerApellido = By.cssSelector("#cphCuerpo_txtPrimerApellido");
	By inpSegundoApellido = By.cssSelector("#cphCuerpo_txtSegundoApellido");

	// TRATAMIENTO DE DATOS
	By inpCheckTratDatos = By.xpath("//label/*[contains(text(),'Acepto')]/preceding-sibling::input");
	By spanTratDatos = By.xpath("//label/*[contains(text(),'Acepto')]/following-sibling::span");
	By inpContinuar = By.xpath("//*[@value='Continuar']");

	// PSEUDOELEMENTO
	String strPseudoElemento = "return window.getComputedStyle(arguments[0],'::after').getPropertyValue('display')";

	/**
	 * Este es el único constructor que tiene esta [BasePageWeb] ya que por sí sola
	 * no puede existir, debe depender de otra [BasePageWeb] que ya haya sido
	 * abierta y que direccione a ésta.
	 */
	public PagePreguntas(BasePageWeb parentPage) {
		super(parentPage);
	}

	public String[] responderPreguntas() throws Exception {
		String respuestasAlmacenadas[] = new String[4];
		int contador = 0;
		WebElement elmPregunta = null;
		boolean seleccionado = false;
//		Evidence.save("FrontPyme-InformaciónPEP", this);
		Evidence.save("FrontPyme-InformaciónPEP");
		do {
			contador++;
		} while (this.element(tituloPrincipal) == null && contador <= 2);

		if (contador > 2) {
			return new String[] { "Las preguntas no se encuentran disponibles para responder" };
		}
		contador = 0;
		// ARREGLO QUE ALMACENA LOS XPATHs A MODIFICAR
		String preguntasReplace[] = { inpPoliticamenteExpuesto, inpCargosPublicos, inpEstaExpuesto, inpEsFamiliar };
		// ARREGLO QUE ALMACENA LAS RESPUESTAS DEL ARCHIVO DE DATOS
		String respuestas[] = { SettingsRun.getTestData().getParameter("Pregunta 1"),
				SettingsRun.getTestData().getParameter("Pregunta 2"),
				SettingsRun.getTestData().getParameter("Pregunta 3"),
				SettingsRun.getTestData().getParameter("Pregunta 4") };

		// REALIZARA CADA ITERACIÓN SOBRE LAS 'RESPUESTAS' EN EL ARREGLO String[]
		for (int i = 0; i < respuestas.length; i++) {

			// SI HA RESPONDIDO "SI" A LA PRIMERA PREGUNTA, PROCEDERA A REALIZAR LAS
			// PREGUNTAS QUE SE VAN A VISUALIZAR
			seleccionado = respuestas[i].equals("SI");
			if (seleccionado) {
				preguntasReplace[i] = preguntasReplace[i].replace("{SELECCION}", "1");
			} else
				preguntasReplace[i] = preguntasReplace[i].replace("{SELECCION}", "2");

			elmPregunta = this.element(By.xpath(preguntasReplace[i]));
			if (elmPregunta != null)
				elmPregunta.click();
			else
				return new String[] { "No fue posible seleccionar una opción" };
//			Evidence.save("FrontPyme-Pregunta" + (i + 1), this);
			Evidence.save("FrontPyme-Pregunta" + (i + 1));

			if (respuestas[2].equals("SI") && i == 2) {
				String cargoPublico = SettingsRun.getTestData().getParameter("Cargo(Público)");
				String estadoVinculacion = SettingsRun.getTestData().getParameter("Estado Vinculación");
				String fechaVinculacion = SettingsRun.getTestData().getParameter("Fecha Vinculación");
				String fechaDesvinculacion = SettingsRun.getTestData().getParameter("Fecha Desvinculación");

				this.selectListItem(slcCargoPublico, cargoPublico);
				this.write(inpFechaDesde, fechaVinculacion);
				WebElement elmCheckVinculado = this.element(spanAcutalVinculado);
				seleccionado = estadoVinculacion.equals("VINCULADO");

				JavascriptExecutor jse = (JavascriptExecutor) this.getDriver();
				String pseudoElemento = (String) jse.executeScript(strPseudoElemento, elmCheckVinculado);

				// valida si el elemento
				if (elmCheckVinculado != null)
					// booleano verifica si la información de la data es vinculado o desvinculado
					if (seleccionado) {
						// si el booleano anterior es true : vinculado ingresa a esta condicion
						// continuando, si el pseudoElemento(contenido de css) contiene (none) quiere
						// decir que no esta
						// seleccionado, se da click en el elemento para seleccionar
						if (pseudoElemento.contains("none"))
							elmCheckVinculado.click();
					} else {
						// si el booleano anterior es false : desvinculado ingresa a esta condicion
						// continuando, si el pseudoElemento(contenido de css) contiene (block) quiere
						// decir que esta
						// seleccionado, se da click en el elemento para deseleccionar
						if (pseudoElemento.contains("block"))
							elmCheckVinculado.click();
						// ingresa valores adicionales para los que ya se encuenytran desvinculados
						DXCUtil.wait(1);
						this.write(inpFechaHasta, fechaDesvinculacion);
					}
				else
					return new String[] { "No se encontro el elemento" };
//				Evidence.save("FrontPyme-Pregunta" + (i + 1), this);
				Evidence.save("FrontPyme-Pregunta" + (i + 1));

			}

			if (respuestas[3].equals("SI") && i == 3) {
//				Evidence.save("FrontPyme-Pregunta" + (i + 1), this);
				Evidence.save("FrontPyme-Pregunta" + (i + 1));

				String parentesco = SettingsRun.getTestData().getParameter("Parentesco");
				this.selectListItem(slcParentesco, parentesco);

				String strNombreFamiliar[] = {
						SettingsRun.getTestData().getParameter("Nombre del familiar").toUpperCase(),
						SettingsRun.getTestData().getParameter("Primer apellido").toUpperCase(),
						SettingsRun.getTestData().getParameter("Segundo apellido").toUpperCase() };
				WebElement emlNombreFamiliar[] = { this.element(inpPrimerNombre), this.element(inpPrimerApellido),
						this.element(inpSegundoApellido) };

				for (int j = 0; j < strNombreFamiliar.length; j++) {
					if (strNombreFamiliar[j].isEmpty())
						return new String[] { "No se diligenciaron campos, por favor verifique" };

					emlNombreFamiliar[j].clear();
					this.write(emlNombreFamiliar[j], strNombreFamiliar[j]);
				}
//				Evidence.save("FrontPyme-Pregunta" + (i + 1), this);
				Evidence.save("FrontPyme-Pregunta" + (i + 1));
			}
			if (respuestas[0].equals("NO")) {
				respuestasAlmacenadas = new String[] {
						"Se ha respondido la pregunta " + (i + 1) + ": " + respuestas[i] + ", según lo esperado",""};
				break;
			}
			DXCUtil.wait(1);
			respuestasAlmacenadas[i] = "Se ha respondido la pregunta " + (i + 1) + ": " + respuestas[i]
					+ ", según lo esperado";
		}

		return respuestasAlmacenadas;
	}

	public String tratamientoDatos(String tratamientoDatos) throws Exception {
		this.pageOrigen = new PageOrigen(this);
		boolean tratamientoDtAceptado = tratamientoDatos.equals("SI");
		WebElement elmCheckTratDatos = this.element(spanTratDatos);
		String msgError = "";
//		Evidence.save("FrontPyme-tratamiendoDatos", this);
		Evidence.save("FrontPyme-tratamiendoDatos");

		JavascriptExecutor jse = (JavascriptExecutor) this.getDriver();
		String pseudoElemento = (String) jse.executeScript(strPseudoElemento, elmCheckTratDatos);

		if (elmCheckTratDatos != null)
			if (tratamientoDtAceptado) {
				if (pseudoElemento.contains("none"))
					elmCheckTratDatos.click();
			} else {
				if (pseudoElemento.contains("block")) {
					elmCheckTratDatos.click();
				}
			}
		else
			return "No se encontro el elemento";

		WebElement elmContinuar = this.element(inpContinuar);
		if (elmContinuar.isEnabled()) {
			elmContinuar.click();
			msgError = this.pageOrigen.getMsgAlertIfExist("lblMasterAlerta");
			if (msgError != null) {
				return msgError;
			}
//			Evidence.save("FrontPyme-tratamiendoDatos", this);
			Evidence.save("FrontPyme-tratamiendoDatos");
		} else
			return "No ha sido posible continuar, por la mayoria de preguntas se enuentran en \"NO\"";

		return "";

	}

}
