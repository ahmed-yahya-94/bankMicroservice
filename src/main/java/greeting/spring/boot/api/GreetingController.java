/*
 * Copyright © 2017 IBM Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package greeting.spring.boot.api;

import java.io.IOException;
import java.util.List;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.IndexField;
import com.cloudant.client.api.model.IndexField.SortOrder;
import com.cloudant.client.api.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import greeting.spring.boot.model.ConsumptionView;
import greeting.spring.boot.model.Greeting;

@RestController
@RequestMapping("/users")
public class GreetingController {

	@Autowired
	private Database db;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Greeting> getAll() throws IOException {
			List<Greeting> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Greeting.class);
			return allDocs;
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public @ResponseBody Greeting getGreeting(@PathVariable String id) throws IOException {
			Greeting greeting = db.find(Greeting.class, id);
			return greeting;
	}
	
	public List<Greeting> getDocByUserName(String userName){
		// Get all documents from socialreviewdb
		 List<Greeting> allDocs = null;
		    try {
		        if (userName == null) {
		            allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Greeting.class);
		        } else {
		            // create Index
		            // Create a design doc named designdoc
		            // A view named querybyitemIdView
		            // and an index named itemId
		        db.createIndex("querybyitemIdView","designdoc","json", new IndexField[]{new IndexField("username",SortOrder.asc)});
		        }
		            System.out.println("Successfully created index");
		            allDocs = db.findByIndex("{\"username\" : " + userName + "}", Greeting.class);
		            
				    ObjectMapper mapper = new ObjectMapper();
					String docJson=mapper.writeValueAsString(allDocs);
					System.out.println("nardooooooooooooooooooooooo yahyaaaaa"+docJson);
		        }
		     catch (Exception e) {
		      System.out.println("Exception thrown : " + e.getMessage());
		      }
		   
		    return allDocs;
	}
	

	@RequestMapping(method=RequestMethod.GET, value="/balance")
	public @ResponseBody ConsumptionView getBalance(@RequestParam String name) {

		Greeting doc=getDocByUserName(name).get(0);
		ConsumptionView balance= new ConsumptionView();
		balance.setName(doc.getName());
		balance.setAccount_number(doc.getAccount_number());
		balance.setCredit_balance(""+ Double.parseDouble(doc.getCredit_balance()));
		balance.setDebit_balance(""+ Double.parseDouble(doc.getDebit_balance()));
		balance.setMaxium_credit(""+ Double.parseDouble(doc.getMaxium_credit()));
		double summary = Double.parseDouble(doc.getDebit_balance()) - Double.parseDouble(doc.getCredit_balance());
		balance.setAccount_summary(""+summary);
		return balance;
	   
	    }
	
	public JSONObject toJsonObject(Greeting doc) {
		JSONObject jobject = new JSONObject();
		jobject.put("_id", doc.get_id());
		jobject.put("_rev", doc.get_rev());
		jobject.put("username", doc.getUsername());
		jobject.put("password", doc.getPassword());
		jobject.put("name", doc.getName());
		jobject.put("account_number", doc.getAccount_number());
		jobject.put("debit_balance", doc.getDebit_balance());
		jobject.put("credit_balance", doc.getCredit_balance());
		jobject.put("maxium_credit", doc.getMaxium_credit());
		jobject.put("statment_id", doc.getStatment_id());
		return jobject;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/transfer")
	public @ResponseBody JSONObject transfer(@RequestParam String name,@RequestParam String accountNumber,@RequestParam String amountStr) throws IOException {
		
		double amount = Double.parseDouble(amountStr);
		
		JSONObject transferObject = new JSONObject();
		System.out.println("username:     "+ name);
		System.out.println("accountNumber:     "+ accountNumber);
	
		List<Greeting> fromDoc=getDocByUserName(name);
		System.out.println("doc sizeeeeeeeeee  "+fromDoc.size()+"         fromDoc.get(0).getDebit_balance())"+ fromDoc.get(0).getDebit_balance() +"   amount: "+ amount);
		if(Double.parseDouble(fromDoc.get(0).getDebit_balance()) >= amount) {
			fromDoc.get(0).setDebit_balance( (Double.parseDouble(fromDoc.get(0).getDebit_balance())- amount) + "");
		} else {
			transferObject.put("Transfer", "Insufficient funds");
			return transferObject;
		}
		
		
		JSONObject fromObject = toJsonObject(fromDoc.get(0));
		db.update(fromObject);
	
		List<Greeting> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Greeting.class);
		
		
		Greeting toDoc= new Greeting ();
		for (int i=0; i<allDocs.size() ;i++) {
			System.out.println("sizeeeee:   "+ allDocs.size()+"                    i=     "+i );
			if(allDocs.get(i).getAccount_number().equals(accountNumber)) {
				toDoc = allDocs.get(i);
				toDoc.setDebit_balance((Double.parseDouble(toDoc.getDebit_balance())+ amount)+"");
				break;
			}
		}
		
		JSONObject toObject = toJsonObject(toDoc);
		db.update(toObject);
		
		transferObject.put("Transfer", "Amount successfully transfered");
		return transferObject;
	}

	@RequestMapping(method=RequestMethod.GET, value="/statement")
	public @ResponseBody JSONObject getStatement(@RequestParam String name) {
		List<Greeting> doc=getDocByUserName(name); 
		JSONObject AdObject = new JSONObject();
		AdObject.put("Statement", doc.get(0).getStatment_id());
		return AdObject;
	   
	    }

	@RequestMapping(method=RequestMethod.GET, value="/login")
	public @ResponseBody JSONObject login(@RequestParam String name) throws IOException {
		
		List<Greeting> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Greeting.class);
		
		JSONObject loginObject = new JSONObject();
		
		
		for (int i=0; i<allDocs.size() ;i++) {
			System.out.println("sizeeeee:   "+ allDocs.size()+"                    i=     "+ i + "            "+allDocs.get(i).getUsername());
			if(allDocs.get(i).getUsername().equals(name)) {
				loginObject.put("Login", allDocs.get(i).getName());
				return loginObject;
			}
		}
		loginObject.put("Login", "can't find the user");
		return loginObject;
	   
	    }

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String add(@RequestBody Greeting greeting) {
			Response response = db.post(greeting);
			String id = response.getId();
			return id;
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public ResponseEntity<?> deleteGreeting(@PathVariable String id) throws IOException {
			Greeting greeting = db.find(Greeting.class, id);
			Response remove = db.remove(greeting.get_id(),greeting.get_rev());
			return new ResponseEntity<String>(remove.getReason(), HttpStatus.valueOf(remove.getStatusCode()));
	}
}