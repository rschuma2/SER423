/*
 * Copyright 2020 Tim Lindquist,
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
 * Purpose: Tab Bar Controller based app. Sharing a Student object among the
 * different view/controllers of the tabs. This is done by extending the
 * UITabBarViewController with a class that defines the model. That model can be
 * accessed in each view controller (and updated) through the tabBarController
 * property of the view controller. The extended class (MyTabBarViewController) must
 * be registered using the storyboard as the custom class for the tab bar controller scene
 * using the identity inspector.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version February, 2020
 */

import Foundation

public class Student {
    public var name: String
    public var studentid: Int
    public var takes: Array<String>
    
    public init(name: String, id: Int){
        self.name = name
        self.studentid = id
        takes = [String]()
    }
    
    public init (jsonStr: String){
        self.name = ""
        self.studentid=0
        self.takes = [String]()
        if let data:Data = jsonStr.data(using: String.Encoding.utf8) as Data?{
            do{
                if let dict:[String:Any] = try JSONSerialization.jsonObject(with: data, options:.mutableContainers) as? [String:Any] {
                    self.name = (dict["name"] as? String)!
                    self.studentid = (dict["studentid"] as? Int)!
                    self.takes = (dict["takes"] as? Array<String>)!
                }
            } catch {
                print("unable to convert Json to a dictionary")
                
            }
        }
    }
    
    public func toJsonString() -> String {
        var jsonStr = "";
        let dict:[String : Any] = ["name": name, "studentid": studentid, "takes":takes] as [String : Any]
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions.prettyPrinted)
            // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: String.Encoding.utf8.rawValue)! as String
        } catch let error as NSError {
            print("unable to convert dictionary to a Json Object with error: \(error)")
        }
        return jsonStr
    }
}
