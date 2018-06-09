/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.modules.forms.ui;

import java.util.ArrayList;
import java.util.List;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.Component;
import org.olat.core.gui.components.velocity.VelocityContainer;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.gui.control.controller.BasicController;
import org.olat.core.util.CodeHelper;
import org.olat.modules.forms.EvaluationFormManager;
import org.olat.modules.forms.EvaluationFormPrintSelection;
import org.olat.modules.forms.EvaluationFormSession;
import org.olat.modules.forms.handler.DefaultReportProvider;
import org.olat.modules.forms.handler.MultipleChoiceTableHandler;
import org.olat.modules.forms.handler.RubricTableHandler;
import org.olat.modules.forms.handler.SingleChoiceTableHandler;
import org.olat.modules.forms.model.jpa.EvaluationFormResponses;
import org.olat.modules.forms.model.xml.Form;
import org.olat.modules.forms.model.xml.MultipleChoice;
import org.olat.modules.forms.model.xml.Rubric;
import org.olat.modules.forms.model.xml.SingleChoice;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Initial date: 01.06.2018<br>
 * 
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class EvaluationFormPrintController extends BasicController {
	
	private VelocityContainer mainVC;
	
	private final Form form;
	private final List<EvaluationFormSession> sessions;
	private final ReportHelper reportHelper;
	
	@Autowired
	private EvaluationFormManager evaluationFormManager;

	public EvaluationFormPrintController(UserRequest ureq, WindowControl wControl, Form form,
			List<EvaluationFormSession> sessions, ReportHelper reportHelper,
			EvaluationFormPrintSelection printSelection) {
		super(ureq, wControl);
		this.form = form;
		this.sessions = sessions;
		this.reportHelper = reportHelper;

		mainVC = createVelocityContainer("report_print");
		if (printSelection.isOverview()) {
			Controller overviewCtrl = new EvaluationFormOverviewController(ureq, getWindowControl(), form, sessions);
			mainVC.put("overview", overviewCtrl.getInitialComponent());
		}
		
		if (printSelection.isTables()) {
			DefaultReportProvider provider = new DefaultReportProvider();
			provider.put(Rubric.TYPE, new RubricTableHandler());
			provider.put(SingleChoice.TYPE, new SingleChoiceTableHandler());
			provider.put(MultipleChoice.TYPE, new MultipleChoiceTableHandler());
			Controller tableReportCtrl = new EvaluationFormReportController(ureq, getWindowControl(), form, sessions,
					provider, reportHelper);
			mainVC.put("tables", tableReportCtrl.getInitialComponent());
		}

		if (printSelection.isDiagrams()) {
			DefaultReportProvider provider = new DefaultReportProvider();
			Controller diagramReportCtrl = new EvaluationFormReportController(ureq, getWindowControl(), form, sessions, provider, reportHelper);
			mainVC.put("diagrams", diagramReportCtrl.getInitialComponent());
		}
		
		if (printSelection.isSessions()) {
			mainVC.contextPut("sessionWrappers", createSessionWrappers(ureq));
		}

		putInitialPanel(mainVC);
	}

	private List<SessionWrapper> createSessionWrappers(UserRequest ureq) {
		List<SessionWrapper> wrappers = new ArrayList<>();
		List<EvaluationFormSession> reloadedSessions = evaluationFormManager.loadSessionsByKey(sessions, 0, -1);
		EvaluationFormResponses responses = evaluationFormManager.loadResponsesBySessions(sessions);
		for (EvaluationFormSession session: reloadedSessions) {
			SessionWrapper wrapper = createSessionWrapper(ureq, session, responses);
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	
	private SessionWrapper createSessionWrapper(UserRequest ureq, EvaluationFormSession session,
			EvaluationFormResponses responses) {
		String componentName = "se_" + CodeHelper.getRAMUniqueID();
		String legendName = reportHelper.getLegend(session).getName();
		Controller controller = new EvaluationFormExecutionController(ureq, getWindowControl(), session,
				responses, form);
		mainVC.put(componentName, controller.getInitialComponent());
		return new SessionWrapper(legendName, componentName);
	}

	@Override
	protected void event(UserRequest ureq, Component source, Event event) {
		//
	}

	@Override
	protected void doDispose() {
		//
	}
	
	public static final class SessionWrapper {

		private final String legendName;
		private final String componentName;
		
		protected SessionWrapper(String legendName, String componentName) {
			super();
			this.legendName = legendName;
			this.componentName = componentName;
		}

		public String getLegendName() {
			return legendName;
		}

		public String getComponentName() {
			return componentName;
		}
		
	}

}