import { useNavigate } from "react-router-dom";
import PasswordUpdateForm from "../component/user/PasswordUpdateForm";
import useVerifyUser from "../utility/customHook/useVerifyUser";

function PasswordUpdate(){
    const [user, ] = useVerifyUser('ROLE_CUSTOMER');

    const navigate = useNavigate();

    return(
        <div className="password-update">
            <PasswordUpdateForm user={user} navigate={(path) => navigate(path)}/>
        </div>
    )
}

export default PasswordUpdate;