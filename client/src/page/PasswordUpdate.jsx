import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";
import { useNavigate } from "react-router-dom";
import PasswordUpdateForm from "../component/user/PasswordUpdateForm";

function PasswordUpdate(){
    const [user, setUser] = useState();

    const navigate = useNavigate();

    useEffect(() => {
        const userData = JSON.parse(sessionStorage.getItem('USER_JWT'));

        if(!userData){
            navigate('/');
            return;
        }

        setUser(userData);
    }, [navigate]);

    return(
        <div className="password-update">
            <NavBar />
            <PasswordUpdateForm user={user} navigate={(path) => navigate(path)}/>
        </div>
    )
}

export default PasswordUpdate;