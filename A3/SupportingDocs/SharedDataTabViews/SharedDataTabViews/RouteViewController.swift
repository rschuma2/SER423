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

import UIKit

class RouteViewController: UIViewController {

    @IBOutlet weak var routeLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let controller:MyTabBarViewController = self.tabBarController as! MyTabBarViewController
        routeLabel.text = "Shared student: \(controller.jim.name)"
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
