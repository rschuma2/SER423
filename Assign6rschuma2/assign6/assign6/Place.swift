/*
 * Copyright 2020 Ryan Schumacher,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ASU and SER423 instructor and staff have the right to build and evaluate this
 * software package for the purpose of determining a grade and program assessment
 *
 * Place.swift
 * assign6
 * Purpose: Swift Client for a Java Place Library JsonRPC server.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Ryan Schumacher mailto:rschuma2@asu.edu
 *         Software Engineering, BSSE, IAFSE, Online
 * @version April, 29, 2020
 */

import Foundation

public class Place {
    open var name: String
    open var description: String
    open var category: String
    open var address_title: String
    open var address_street: String
    open var image: String
    open var elevation: Double
    open var latitude: Double
    open var longitude: Double
    
    init (jsonStr: String){
        self.name = ""
        self.description = ""
        self.category = ""
        self.address_title = ""
        self.address_street = ""
        self.image = ""
        self.elevation = 0.0
        self.latitude = 0.0
        self.longitude = 0.0
        if let data: Data = jsonStr.data(using: String.Encoding.utf8){
            do{
                let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:AnyObject]
                self.name = (dict!["name"] as? String)!
                self.description = (dict!["description"] as? String)!
                self.category = (dict!["category"] as? String)!
                self.address_street = (dict!["address-street"] as? String)!
                self.address_title = (dict!["address-title"] as? String)!
                self.image = (dict!["image"] as? String)!
                self.elevation = (dict!["elevation"] as? Double)!
                self.latitude = (dict!["latitude"] as? Double)!
                self.longitude = (dict!["longitude"] as? Double)!
            } catch {
                print("unable to convert to dictionary")
                
            }
        }
    }
    
    init(dict: [String:Any]){
        self.name = dict["name"] as! String
        self.description = dict["description"] as! String
        self.category = dict["category"] as! String
        self.address_title = dict["address-title"] as! String
        self.address_street = dict["address-street"] as! String
        self.image = dict["image"] as! String
        self.elevation = dict["elevation"] as! Double
        self.latitude = dict["latitude"] as! Double
        self.longitude = dict["longitude"] as! Double
    }
    
    func toJsonString() -> String {
        var jsonStr = "";
        let dict = ["name": name, "description": description, "category":category, "address-title": address_title, "address-street": address_street, "image": image, "elevation": elevation, "latitude":latitude, "longitude": longitude] as [String : Any]
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions.prettyPrinted)
            jsonStr = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        } catch let error as NSError {
            print(error)
        }
        return jsonStr
    }

    func toDict() -> [String:Any] {
        let dict:[String:Any] = ["name": name, "description": description, "category":category, "address-title": address_title, "address-street": address_street, "image": image, "elevation": elevation, "latitude":latitude, "longitude": longitude] as [String : Any]
        return dict
    }
}
