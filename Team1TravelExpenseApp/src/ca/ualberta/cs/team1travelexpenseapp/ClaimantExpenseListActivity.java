package ca.ualberta.cs.team1travelexpenseapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ClaimantExpenseListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claimant_expense_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claimant_expense_list, menu);
		return true;
	}

}
