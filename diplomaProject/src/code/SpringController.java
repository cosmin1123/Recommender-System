@RestController
public class UserAddController {

    @RequestMapping(method = RequestMethod.POST, value = "/useradd")
    public void userAdd(@RequestParam(value = "userId", defaultValue = "") String userId) {
        // execute actions
    }
}