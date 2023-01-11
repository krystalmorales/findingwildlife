import React, { useContext, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import UserContext from "../UserContext";

import Error from "./Error";

function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState([]);

    const history = useHistory();
    const authManager = useContext(UserContext);

    const handleSubmit = async (event) => {
        event.preventDefault();

        const response = await fetch("http://localhost:8080/authenticate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                password,
            }),
        });

        if (response.status === 200) {
            const { jwt_token } = await response.json();
            authManager.login(jwt_token);
            history.push("/");
        } else if (response.status === 403) {
            setErrors(["Login failed."]);
        } else {
            setErrors(["Unknown Error."]);
        }
    };

    return (
        <div className="login-container">
            <h2 className="pt-3 text-center">Login</h2>
            {errors.length > 0 ? <Error errors={errors}/> : null}
            <form onSubmit={handleSubmit}>
                <div className="pt-3">
                    <label hmtlfor="username">Username</label>
                    <input name="username" type="text" className="form-control" id="username" value={username} onChange={(event) => setUsername(event.target.value)} />
                </div>
                <div className="pt-3">
                    <label htmlfor="password">Password</label>
                    <input type="password" className="form-control" onChange={(event) => setPassword(event.target.value)} id="password" />
                </div>
                <div className="pt-3">
                    <button type="submit" className='btn btn-primary'>Login</button>
                </div>
            </form>
        </div>
    )
}

export default Login;