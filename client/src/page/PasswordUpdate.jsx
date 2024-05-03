import { useNavigate } from "react-router-dom";
import PasswordUpdateForm from "../component/user/PasswordUpdateForm";
import useVerifyUserRole from "../utility/customHook/useVerifyUserRole";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";

function PasswordUpdate(){
    const isAccessible = useVerifyUserAccess('/verification/user/password');
    const [user, ] = useVerifyUserRole('ROLE_CUSTOMER');

    const navigate = useNavigate();

    return(
        <div className="password-update">
            {isAccessible &&
                <PasswordUpdateForm user={user} navigate={(path) => navigate(path)}/>        
            }
        </div>
    )
}

export default PasswordUpdate;