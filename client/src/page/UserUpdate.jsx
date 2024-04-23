import { Link } from "react-router-dom";
import UserUpdateForm from "../component/user/UserUpdateForm";

function UserUpdate(){

    return(
        <div className="user-update-container">
            <UserUpdateForm />
            <Link className="change-password" to={'/user/update/password'}>
                <button>Change password</button>
            </Link>
        </div>
    )
}

export default UserUpdate;