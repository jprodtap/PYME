package dav.Consultas_Y_Extractos;

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

public class PageCheques extends PagePortalPymes{

	By tituloCheque = By.id("lblMasterTitulo");
	public PageCheques(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}
	
	By ListProduc = By.id("cphCuerpo_ddCuentaCredito");
	By formatoDesc = By.id("cphCuerpo_ddlDescarga");
	By nomArchDes = By.id("cphCuerpo_txtNombreArchivo");
	By btnDesArch = By.id("cphCuerpo_btnDescargaMasiva");
	By seleArch = By.id("cphCuerpo_ddlDescargaMasiva");
	
//=================[CHEQUE DEVUELTOS]================================================================	
	By ListProduc2 = By.id("cphCuerpo_ddProducto");
	
	PageOrigen pageOrigen = null;
	
	public String Cheques(String tipoProducto, String numeroProducto) throws Exception {
		this.pageOrigen = new PageOrigen(this);
		int contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador > 3) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el titulo : Cheques");
				this.pageOrigen.terminarIteracion();
			}
			
		} while (this.element(tituloCheque) == null);
		String msgError = this.lisProducto(ListProduc, tipoProducto, numeroProducto);
		if (msgError != null) {
			return msgError;
		}
		Evidence.saveFullPage("Cheques", this);
		
		do {
			contador++;
			DXCUtil.wait(8);
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
				this.pageOrigen.terminarIteracion();
			}
		} while (this.element(formatoDesc) == null);
		String descargarArch = SettingsRun.getTestData().getParameter("Formato de Descarga").trim();
		msgError = this.selectListItem2(this.element(formatoDesc), descargarArch);
		Evidence.saveFullPage("Des", this);

		if (!msgError.isEmpty()) {
			return  msgError;
		}
		DXCUtil.wait(8);
		if (this.element(nomArchDes) != null
				&& this.isEnabled(this.element(nomArchDes))) {
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
		} while (this.element(btnDesArch) == null);
		this.click(this.element(btnDesArch));

		DXCUtil.wait(6);
		Evidence.saveFullPage("ndsjd",this);
		return msgError;
		
		
	}

	
	public String Chequesdevuel(String tipoProducto, String numeroProducto) throws Exception {
		this.pageOrigen = new PageOrigen(this);
		int contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (contador > 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el titulo : Cheques");
				this.pageOrigen.terminarIteracion();
			}
			
		} while (this.element(tituloCheque) == null);
		
		String msgError = this.lisProducto(ListProduc2, tipoProducto, numeroProducto);
		if (msgError != null) {
			return msgError;
		}
		Evidence.saveFullPage("Cheques", this);
		
		
		if (this.element(formatoDesc) != null) {
			do {
				contador++;
				DXCUtil.wait(8);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(formatoDesc) == null);
			String descargarArch = SettingsRun.getTestData().getParameter("Formato de Descarga").trim();
			msgError = this.selectListItem2(this.element(formatoDesc), descargarArch);
			Evidence.saveFullPage("Des", this);

			if (!msgError.isEmpty()) {
				return  msgError;
			}
		}
		
		if (this.element(btnDesArch) != null) {
			do {
				contador++;
				DXCUtil.wait(8);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el :");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(btnDesArch) == null);
			String descargarArch = SettingsRun.getTestData().getParameter("Formato de Descarga").trim();
			msgError = this.selectListItem2(this.element(seleArch), descargarArch);
			Evidence.saveFullPage("Des", this);

			if (!msgError.isEmpty()) {
				return  msgError;
			}
		}
		
		DXCUtil.wait(8);
		if (this.element(nomArchDes) != null
				&& this.isEnabled(this.element(nomArchDes))) {
			String descargaArch = "DescargarMovimiento" + SettingsRun.getCurrentIteration();
			this.write(this.element(nomArchDes), descargaArch);
		}
		
		if (this.element(btnDesArch) != null) {
			do {
				contador++;
				DXCUtil.wait(5);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se encontro el botón Descargar");
					this.pageOrigen.terminarIteracion();
				}
			} while (this.element(btnDesArch) == null);
			this.click(this.element(btnDesArch));

			DXCUtil.wait(6);
			Reporter.reportEvent(Reporter.MIC_PASS, "Se Descargo el Archivo");
			Evidence.saveFullPage("Descarga",this);
		}
		
		return msgError;
		
		
	}
}
