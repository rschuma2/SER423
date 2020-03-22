/*
*
* PlaceDescription.swift
* DeleteMePls
*
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
*
* Purpose: Facilitates the creation of PlaceDescription objects.
*
* @author Ryan Schumacher rschuma2@asu.edu
*         Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/

import Foundation

/*
 * Here is a simple student class that initializes from Json and deserializes to a Json string.
 * It provides an initializer to create an instance from a json string, and
 * a method to covert an instance back into a json string. The underlying
 * NSJSONSerialization class that's used has been in the api for some time and is the same as
 * used with Objective-C. Accessed in Swift using JSONSerialization. You should use this class
 * as the basis for creating your solution to the lab exercises. Also note that the code assumes
 * for json string format is correct. As with most assignments and examples we'll cover, input
 * validation is ignored, and can be ignored should you be asked to submit code in this course.
 */
class PlaceDescription {
    var name: String
    var description: String
    var category: String
    var addressTitle: String
    var addressStreet: Array<String>
    var elevation: Double
    var latitude: Double
    var longitude: Double
    
    init (jsonStr: String){
        self.name = ""
        self.description = ""
        self.category = ""
        self.addressTitle = ""
        self.addressStreet = [String]()
        self.elevation = 0.0
        self.latitude = 0.0
        self.longitude = 0.0
        if let data: Data = jsonStr.data(using: String.Encoding.utf8){
            do{
                let dict = try JSONSerialization.jsonObject(with: data,options:.mutableContainers) as?[String:Any]
                self.name = (dict!["name"] as? String)!
                self.description = (dict!["description"] as? String)!
                self.category = (dict!["category"] as? String)!
                self.addressTitle = (dict!["address-title"] as? String)!
                self.addressStreet = (dict!["address-street"] as? Array<String>)!
                self.elevation = (dict!["elevation"] as? Double)!
                self.latitude = (dict!["latitude"] as? Double)!
                self.longitude = (dict!["longitude"] as? Double)!
            } catch {
                print("unable to convert to dictionary")
                
            }
        }
    }
    
    func toJsonString() -> String {
        var jsonStr = "";
        let dict:[String:Any] = ["name": name, "description": description, "category": category, "ç":addressTitle, "addressStreet":addressStreet, "elevation": elevation, "latitude": latitude, "longtitude": longitude] as [String : Any]
        do {
            let jsonData:Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions.prettyPrinted)
            // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        } catch let error as NSError {
            print(error)
        }
        return jsonStr
    }


}

/*
func getUser (name: String) -> PlaceDescription?{
    var ret: PlaceDescription? = nil
    if name.isEqual("Jim"){
        let aUser = PlaceDescription()
        aUser.userId = "JBuffet"
        ret = aUser
    } else if name.isEqual("Tim") {
        let aUser = PlaceDescription()
        // Tim has a User, but he has no ID
        ret = aUser
    }
    // Sue (or any other name) is a nil User
    return ret
}
*/

