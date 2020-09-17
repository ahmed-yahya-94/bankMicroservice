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

package greeting.spring.boot.model;

public class Greeting {
	
	
	/**
	 * Revision of the document in the cloudant database. Cloudant will create this
	 * value for new documents.
	 */
	
	
	private String _id;

	private String _rev;
	
	private String username;

	private String password;
	
	private String name;
	
	private String account_number;
	
	private String debit_balance;

	private String credit_balance;

	private String maxium_credit;

	private String statment_id;
	
	
	/**
	 * ID of the document in the cloudant database Cloudant will create this value
	 * for new documents.
	 */

	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getDebit_balance() {
		return debit_balance;
	}

	public void setDebit_balance(String debit_balance) {
		this.debit_balance = debit_balance;
	}

	public String getCredit_balance() {
		return credit_balance;
	}

	public void setCredit_balance(String credit_balance) {
		this.credit_balance = credit_balance;
	}

	public String getMaxium_credit() {
		return maxium_credit;
	}

	public void setMaxium_credit(String maxium_credit) {
		this.maxium_credit = maxium_credit;
	}

	public String getStatment_id() {
		return statment_id;
	}

	public void setStatment_id(String statment_id) {
		this.statment_id = statment_id;
	}
}
