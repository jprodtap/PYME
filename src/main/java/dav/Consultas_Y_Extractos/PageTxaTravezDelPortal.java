package dav.Consultas_Y_Extractos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;

import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageTxaTravezDelPortal extends PagePortalPymes {

	String tiempoconsulta;

	By btnBusqAvanzada = By.id("cphCuerpo_lnkBusquedaAvanzada");
	By btnMostDatos = By.id("cphCuerpo_btnMostrarDatos");
	By btn48 = By.xpath("//*[@id='cphCuerpo_rdbUltimas48Horas' or@id='cphCuerpo_rdbBusqueda48Horas']");
	By btn7 = By.xpath("//*[@id='cphCuerpo_rdbUltimos7dias' or @id='cphCuerpo_rdbBusqueda7Dias']");
	By btn7segundo = By.id("cphCuerpo_rdbBusqueda7Dias");
	By btn30 = By.id("cphCuerpo_rdbBusqueda30Dias");
	By lisFormatoDes = By.id("cphCuerpo_dropFormatos");
	By nomArchDes = By.id("cphCuerpo_txtNombreArchivo");
	By btnDescarga = By.id("cphCuerpo_btnExportar");

	By ListProduc2 = By.id("cphCuerpo_ddlBusAvanzadaProducto");
	By cmpFechadesde = By.id("cphCuerpo_txtFechaDesde");
	By cmpFechaInicio = By.id("cphCuerpo_txtFechaHasta");

	By table = By.id("cphCuerpo_gvTransacciones");
	By btnSiguiente = By.id("cphCuerpo_lnkSiguiente");
	By btnSiguienteDisable = By.xpath("//*[@id='cphCuerpo_lnkSiguiente' and @class='aspNetDisabled']");

	public PageTxaTravezDelPortal(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	PageOrigen pageOrigen = null;

	public String TxDesdeElPortalDes(String tipoProducto, String numeroProducto, boolean Descarga) throws Exception {
		this.pageOrigen = new PageOrigen(this);
		this.tiempoconsulta = SettingsRun.getTestData().getParameter("Tiempo de Consulta").trim();
		int contador = 0;
		String msgError = null;
		String msg = null;

		do {
			DXCUtil.wait(1);
		} while (this.element(btnBusqAvanzada) == null);

		if (this.tiempoconsulta.contains("m�s criterios")) {
			Date fecha = new Date();

			Date fechaDesdes = DXCUtil.dateAdd(fecha, Calendar.DAY_OF_MONTH, -30);
			String today = DXCUtil.dateToString(fechaDesdes, "dd/mm/yyyy");
			Date fecha2 = new Date();
			String fechain = DXCUtil.dateToString(fecha2, "dd/mm/yyyy");

			this.waitIngresoModulo("B�squeda Avanzada de Movimientos");

			do {
				contador++;
				DXCUtil.wait(7);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(ListProduc2) == null);
			msgError = this.lisProducto(ListProduc2, tipoProducto, numeroProducto);
			if (msgError != null) {
				return msgError;
			}

			this.write(cmpFechadesde, today);
			this.write(cmpFechaInicio, fechain);

			this.click(this.element(btnMostDatos));

			Evidence.save("Mas Datos");
			this.click(this.element(btnBusqAvanzada));
			DXCUtil.wait(2);
			this.click(this.element(btnMostDatos));

		} else if (this.tiempoconsulta.contains("48 horas")) {
			this.element(btn48).click();
			DXCUtil.wait(1);
			this.click(this.element(btnMostDatos));
		} else if (this.tiempoconsulta.contains("7 d�as")) {
			if (this.element(btn7)!= null) {
				this.click(this.element(btn7));
			}
			if (this.element(btn7segundo)!= null) {
				this.click(this.element(btn7segundo));
			}
			DXCUtil.wait(1);
			this.click(this.element(btnMostDatos));
		} else if (this.tiempoconsulta.contains("30 d�as")) {
			this.click(this.element(btn30));
			DXCUtil.wait(1);
			this.click(this.element(btnMostDatos));
		}
//		Evidence.save("MOvimientos", this);

		String msgAlerta = this.getMsgAlertIfExist("lblMasterAlerta");
		if (msgAlerta != null && msgAlerta.equals("NO SE ENCONTRARON DATOS")) {
			return msgAlerta;
		}

		do {
			DXCUtil.wait(1);
		} while (this.element(table) == null);

		String[] ghjs = null;
		int contadorSiguiente = 0;

		if (this.element(btnSiguiente) == null) {

			Evidence.saveFullPage("Movimientos", this);

//			ghjs = this.TabelMovi();

		} else {
			// Se utiliza una lista para acumular datos de todas las p�ginas
//			List<String> moviList = new ArrayList<>(); 
			boolean ver;
			do {
				contadorSiguiente++;
				DXCUtil.wait(1);
				Evidence.saveFullPage("Movimientos pag" + contadorSiguiente, this);

				// Capture data from the current page and add to the list
//				String[] currentPageData = this.TabelMovi();
//				moviList.addAll(Arrays.asList(currentPageData));

				DXCUtil.wait(10);

				if (this.element(btnSiguiente) != null && this.element(btnSiguienteDisable) == null) {

					this.click(btnSiguiente);

					ver = true;

				} else {
					ver = false;
				}

			} while (ver != false);

			// Convert the List to an array
//			ghjs = moviList.toArray(new String[0]);
		}

		if (Descarga) {
			do {
				contador++;
				DXCUtil.wait(8);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(lisFormatoDes) == null);
			String descargarArch = SettingsRun.getTestData().getParameter("Formato de Descarga").trim();
			msgError = this.selectListItem2(this.element(lisFormatoDes), descargarArch);
			Evidence.saveFullPage("Des", this);
			if (!msgError.isEmpty()) {
				return msgError;
			}
			
			DXCUtil.wait(8);
			if (this.element(nomArchDes) != null && this.isEnabled(this.element(nomArchDes))) {
				String descargaArch = "DescargarMovimiento" + SettingsRun.getCurrentIteration();
				this.write(this.element(nomArchDes), descargaArch);
			}
			do {
				contador++;
				DXCUtil.wait(5);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(btnDescarga) == null);
			this.click(this.element(btnDescarga));

			DXCUtil.wait(6);
			Evidence.save("ndsjd");
		}
		return msgError;
	}
}
