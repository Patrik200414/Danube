import { useNavigate } from "react-router-dom";
import PasswordUpdateForm from "../component/user/PasswordUpdateForm";
import useVerifyUserRole from "../utility/customHook/useVerifyUserRole";

function PasswordUpdate(){
    const [user, ] = useVerifyUserRole('ROLE_CUSTOMER');

    const navigate = useNavigate();

    return(
        <div className="password-update">
            <PasswordUpdateForm user={user} navigate={(path) => navigate(path)}/>
        </div>
    )
}

export default PasswordUpdate;