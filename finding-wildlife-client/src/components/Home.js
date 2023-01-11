import { useContext } from "react";
import UserContext from "../UserContext";

function Home(){

    const authManager = useContext(UserContext);

    return(
        <div className="home-container">
            {authManager.user ? <h1 className="home-h1">Welcome to Finding Wildlife, {authManager.user.username}</h1> : <h1 className="home-h1">Welcome to Finding Wildlife</h1>}
        </div>
    );
    
}

export default Home;