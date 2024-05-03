import { Link } from "react-router-dom";
import UserUpdateForm from "../component/user/UserUpdateForm";
import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";

function UserUpdate(){
    const isAccessible = useVerifyUserAccess('/verification/user/update');

    return(
        <div className="user-update-container">
            {isAccessible && 
                <>
                    <UserUpdateForm />
                    <Link className="change-password" to={'/user/password'}>
                        <button>Change password</button>
                    </Link>
                </>
            }
            
        </div>
    )
}

export default UserUpdate;