import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";
import { useNavigate } from "react-router-dom";
import PasswordUpdateForm from "../component/PasswordUpdateForm";

function PasswordUpdate(){
    const [user, setUser] = useState();

    const navigate = useNavigate();

    useEffect(() => {
        const userData = JSON.stringify(sessionStorage.getItem('USER_JWT'));

        if(!userData){
            navigate('/');
            return;
        }

        setUser(userData);
    }, []);

    return(
        <div className="password-update">
            <NavBar />
            <PasswordUpdateForm />
        </div>
    )
}

export default PasswordUpdate;