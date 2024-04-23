import { Link } from "react-router-dom";
import NavBar from "../component/NavBar";
import UserUpdateForm from "../component/user/UserUpdateForm";

function UserUpdate(){

    return(
        <div className="user-update-container">
            <NavBar />
            <UserUpdateForm />
            <Link className="change-password" to={'/user/update/password'}>
                <button>Change password</button>
            </Link>
        </div>
    )
}

export default UserUpdate;