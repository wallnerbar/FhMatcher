package swenga.controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import swenga.dao.AnswerDao;
import swenga.dao.ProfileDao;
import swenga.dao.QuestionDao;
import swenga.model.AnswersModel;
import swenga.model.QuestionModel;

@Controller
public class QuestionController {

	@Autowired
	QuestionDao questionDao;
	
	@Autowired
	AnswerDao answerDao;
	
	@Autowired
	ProfileDao profileDao;

	public String getUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
	
	@RequestMapping(value = "/questionNext{questionVal}", method = { RequestMethod.GET, RequestMethod.POST })
	public String question(Model model, @PathVariable("questionVal") int questionVal) {
		int questionAtm = questionVal + 1;
		
		if (questionAtm == 9) {
			return "redirect:/profile/"+getUsername();
		} else {
			QuestionModel question = questionDao.getQuestionByID(questionAtm);
			model.addAttribute("question", question);
			model.addAttribute("questionAtm", questionAtm);

			return "question";
		}
	}
	
	@RequestMapping(value = "/question", method = { RequestMethod.GET, RequestMethod.POST })
	public String question(Model model) {
		int questionAtm = 1;
		QuestionModel question = questionDao.getQuestionByID(questionAtm);
		model.addAttribute("question", question);
		model.addAttribute("questionAtm", questionAtm);
		return "question";
	}
	
	@RequestMapping("/fillAnswers")
	public String fillAnswers(Principal principal,Model model, @RequestParam("answer") String answer, @RequestParam("questionVal") int questionID) {
		
		principal.getName();
		AnswersModel a1 = new AnswersModel(Boolean.valueOf(answer));
		a1.setProfiles(profileDao.getProfileByUsername(getUsername()));
	    a1.setQuestions(questionDao.getQuestionByID(questionID));
		answerDao.merge(a1);
		System.out.println(profileDao.getProfileByUsername(getUsername()).getId());
		System.out.println(questionDao.getQuestionByID(questionID).getDescription());
		return "forward:/questionNext"+questionID;
	}
	
	@RequestMapping("/dropAnswers")
	public String dropAnswers() {
		Set<AnswersModel> answers = profileDao.getProfileByUsername(getUsername()).getAnswers();
		profileDao.getProfileByUsername(getUsername()).setAnswers(null);
		for(AnswersModel answer : answers) {
			answerDao.deleteQuestionWithId(answer.getId());
		}
		return "redirect:/fillQuestions";
	}
	
	@RequestMapping("/fillQuestions")
	@Transactional
	public String fillQuestions(Model model) {

		if (questionDao.isTableEmpty()) {
			QuestionModel q1 = new QuestionModel("Interessiert in? [m/w]");
			questionDao.persist(q1);
			QuestionModel q2 = new QuestionModel("Morgen oder Abendmensch? [morgen/abend]");
			questionDao.persist(q2);
			QuestionModel q3 = new QuestionModel("Hunde- oder Katzenmensch? [wau/miau]");
			questionDao.persist(q3);
			QuestionModel q4 = new QuestionModel("Rock- oder Dancemusikliebhaber? [rock/dance]");
			questionDao.persist(q4);
			QuestionModel q5 = new QuestionModel("Stadt- oder Landmensch? [stadt/land]");
			questionDao.persist(q5);
			QuestionModel q6 = new QuestionModel("Unternehmenslustig oder Stubenhocker? [pardey/fernsehn]");
			questionDao.persist(q6);
			QuestionModel q7 = new QuestionModel("Bier- oder Weintrangla? [bier/wein]");
			questionDao.persist(q7);
			QuestionModel q8 = new QuestionModel("Oranges oder Gr�nes Twinni? [orange/gr�n]");
			questionDao.persist(q8);
		}
		return "forward:/question";
	}

	/*
	 * @ExceptionHandler(Exception.class) public String handleAllException(Exception
	 * ex) {
	 * 
	 * return "error";
	 * 
	 * }
	 */
}
