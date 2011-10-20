import models.CarteModele;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if (CarteModele.count() == 0) {
			System.out.println("charge cpqf");
			//Fixtures.loadModels("data-cartes.yml");
			Fixtures.loadModels("data-Innistrad.yml");
		}
	}

}
