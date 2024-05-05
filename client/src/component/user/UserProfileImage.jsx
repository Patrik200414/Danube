import { Link } from "react-router-dom"
import userImage from "../../static/userImage.svg";
import PropTypes from "prop-types";

function UserProfileImage({userFirstName}){
    return(
        <Link to='/profile'>
            <div className="greeting">
                <img className="user-image" src={userImage} />
                <div>
                    <h5>Welcome</h5>
                    <p>{userFirstName}</p>
                </div>
            </div>
        </Link>
    )
}

UserProfileImage.propTypes = {
    userFirstName: PropTypes.string
}



export default UserProfileImage;