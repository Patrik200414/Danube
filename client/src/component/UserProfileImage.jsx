import { Link } from "react-router-dom"
import userImage from "../static/userImage.svg";

function UserProfileImage({user}){
    return(
        <Link to='/profile'>
            <div className="greeting">
                <img className="user-image" src={userImage} />
                <div>
                    <h5>Welcome</h5>
                    <p>{user.firstName}</p>
                </div>
            </div>
        </Link>
    )
}

export default UserProfileImage;