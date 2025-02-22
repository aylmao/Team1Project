/*
Copyright 2015 Jeffrey Oduro, Cody Ingram, Boyan Peychoff, Kenny Young, Dennis Truong, Victor Olivares 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


//taken and modified from http://v4all123.blogspot.in/2013/09/spinner-with-multiple-selection-in.html (March 14 2015), used with permission of author
package views;  
  
import java.util.ArrayList;
import java.util.Arrays;  
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;  
import java.util.List;  
  


import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.R.id;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import android.app.Activity;
import android.app.AlertDialog;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;  
import android.util.AttributeSet;  
import android.util.Log;
import android.widget.ArrayAdapter;  
import android.widget.ListView;
import android.widget.Spinner;  
import android.widget.SpinnerAdapter;  
/**
 * Implements a "MultiSelectionSpinner", when clicked it presents a dialog allowing for selection of a subset of items from a
 * list using checkboxes. Selected items will then show as a comma separated list on the face of the spinner.
 * The methods allow setting and getting of the selected items, as well as the entire list of possible items.
 * It can store an arrayList of arbitrary objects in order to recover the selected objects directly.
 *
 */
public class MultiSelectionSpinner extends Spinner implements  
  OnMultiChoiceClickListener {  
 String[] _items = null;  
 boolean[] mSelection = null;
 ArrayList<?> itemsList  = null;
 Context context;
  
 ArrayAdapter<String> simple_adapter;
  
 public MultiSelectionSpinner(Context context) {  
  super(context);  
  this.context = context;
  simple_adapter = new ArrayAdapter<String>(context,  
    android.R.layout.simple_spinner_item);  
  super.setAdapter(simple_adapter);  
 }  
  

 public MultiSelectionSpinner(Context context, AttributeSet attrs) {  
  super(context, attrs);  
  this.context = context;
  simple_adapter = new ArrayAdapter<String>(context,  
    android.R.layout.simple_spinner_item);  
  super.setAdapter(simple_adapter);  
 }  
  
 
 public void onClick(DialogInterface dialog, int which, boolean isChecked) {  
  if (mSelection != null && which < mSelection.length) {  
   mSelection[which] = isChecked;  
  
   simple_adapter.clear();  
   simple_adapter.add(buildSelectedItemString());  
  } else {  
   throw new IllegalArgumentException(  
     "Argument 'which' is out of bounds.");  
  }  
 }  
  
 @Override  
 public boolean performClick() {  
  AlertDialog.Builder builder = new AlertDialog.Builder(getContext());  
  builder.setMultiChoiceItems(_items, mSelection, this);
  
  builder.setOnDismissListener(new OnDismissListener() {
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) findViewById(R.id.claimFilterSpinner);
		try {
			Claimant user=(Claimant) UserSingleton.getUserSingleton().getUser();
			ClaimantClaimsListActivity activity = (ClaimantClaimsListActivity) getContext();
			ArrayList<Claim> displayList = activity.getDisplayList();
			ArrayAdapter<Claim> claimsAdapter = activity.getArrayAdapter();
			List<String> tags = tagSpinner.getSelectedStrings();
			
			if(tags.size() == 0){
				displayList.clear();
				Collection<Claim> claims = user.getClaimList().getClaims();
		  		displayList.addAll(claims);
				claimsAdapter.notifyDataSetChanged();
			} else {
				displayList.clear();
				Collection<Claim> claims = user.getClaimList().getClaims();
		  		displayList.addAll(claims);
				ArrayList<Claim> list = new ArrayList<Claim>();
				for(Claim claim: displayList){
					for(String tag: claim.getClaimTagNameList()) {
						if(tags.contains(tag)){
							list.add(claim);
						}
					}
				}
				displayList.clear();
		  		displayList.addAll(list);
				claimsAdapter.notifyDataSetChanged();
			}
		} catch(Exception e) {
			
		}
	}
  });
  
  builder.show();  
  return true;  
 }  
  
 @Override  
 public void setAdapter(SpinnerAdapter adapter) {  
  throw new RuntimeException(  
    "setAdapter is not supported by MultiSelectSpinner.");  
 }  
  
 public void setItems(String[] items) {  
  _items = items;  
  mSelection = new boolean[_items.length];  
  simple_adapter.clear();   
  Arrays.fill(mSelection, false);  
 }  
  
 public void setItems(List<String> items) {  
  _items = items.toArray(new String[items.size()]);  
  mSelection = new boolean[_items.length];  
  simple_adapter.clear();   
  Arrays.fill(mSelection, false);  
 }  
  
 public void setItems(ArrayList items) { 
	 itemsList=items;
	 ArrayList<String> stringList = new ArrayList<String>();
	  for(Object i : items){
		  stringList.add(i.toString());  
	  }
	  _items = stringList.toArray(new String[items.size()]);  
	  mSelection = new boolean[_items.length];  
	  simple_adapter.clear();    
	  Arrays.fill(mSelection, false);  
	 }  
 
 public void setSelection(String[] selection) {  
  for (String cell : selection) {  
   for (int j = 0; j < _items.length; ++j) {  
    if (_items[j].equals(cell)) {  
     mSelection[j] = true;  
    }  
   }  
  }  
 }  
  
 public void setSelection(List<String> selection) {  
  for (int i = 0; i < mSelection.length; i++) {  
   mSelection[i] = false;  
  }  
  for (String sel : selection) {  
   for (int j = 0; j < _items.length; ++j) {  
    if (_items[j].equals(sel)) {  
     mSelection[j] = true;  
    }  
   }  
  }  
  simple_adapter.clear();  
  simple_adapter.add(buildSelectedItemString());  
 }  
 
 public void setSelection(ArrayList selection) {  
	  for (int i = 0; i < mSelection.length; i++) {  
	   mSelection[i] = false;  
	  }  
	  for (Object sel : selection) {  
	   for (int j = 0; j < _items.length; ++j) {  
	    if (itemsList.get(j).equals(sel)) {  
	     mSelection[j] = true;  
	    }  
	   }  
	  }  
	  simple_adapter.clear();  
	  simple_adapter.add(buildSelectedItemString());  
	 } 
  
 public void setSelection(int index) {  
  for (int i = 0; i < mSelection.length; i++) {  
   mSelection[i] = false;  
  }  
  if (index >= 0 && index < mSelection.length) {  
   mSelection[index] = true;  
  } else {  
   throw new IllegalArgumentException("Index " + index  
     + " is out of bounds.");  
  }  
  simple_adapter.clear();  
  simple_adapter.add(buildSelectedItemString());  
 }  
  
 public void setSelection(int[] selectedIndicies) {  
  for (int i = 0; i < mSelection.length; i++) {  
   mSelection[i] = false;  
  }  
  for (int index : selectedIndicies) {  
   if (index >= 0 && index < mSelection.length) {  
    mSelection[index] = true;  
   } else {  
    throw new IllegalArgumentException("Index " + index  
      + " is out of bounds.");  
   }     
  }  
  simple_adapter.clear();  
  simple_adapter.add(buildSelectedItemString());  
 }  
  
 public List<String> getSelectedStrings() {  
  List<String> selection = new LinkedList<String>();  
  for (int i = 0; i < _items.length; ++i) {  
   if (mSelection[i]) {  
    selection.add(_items[i]);  
   }  
  }  
  return selection;  
 }
 
 public ArrayList getSelectedItems() {  
	 	ArrayList selectedItems = new ArrayList();
	 	for(int i = 0; i < mSelection.length; i++){
	 		if(mSelection[i]){
	 			selectedItems.add(itemsList.get(i));
	 		}
	 	}
	 	return selectedItems;
	 }  
  
 public List<Integer> getSelectedIndicies() {  
  List<Integer> selection = new LinkedList<Integer>();  
  for (int i = 0; i < _items.length; ++i) {  
   if (mSelection[i]) {  
    selection.add(i);  
   }  
  }  
  return selection;  
 }  
  
 private String buildSelectedItemString() {  
  StringBuilder sb = new StringBuilder();  
  boolean foundOne = false;  
  
  for (int i = 0; i < _items.length; ++i) {  
   if (mSelection[i]) {  
    if (foundOne) {  
     sb.append(", ");  
    }  
    foundOne = true;  
  
    sb.append(_items[i]);  
   }  
  }  
  return sb.toString();  
 }  
  
 public String getSelectedItemsAsString() {  
   StringBuilder sb = new StringBuilder();  
   boolean foundOne = false;  
  
   for (int i = 0; i < _items.length; ++i) {  
    if (mSelection[i]) {  
     if (foundOne) {  
       sb.append(", ");  
     }  
     foundOne = true;  
     sb.append(_items[i]);  
    }  
   }  
   return sb.toString();  
 }  
}  