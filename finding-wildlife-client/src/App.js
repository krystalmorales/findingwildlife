import { BrowserRouter as Router, Link, Redirect, Route, Switch } from 'react-router-dom';
import { useState } from 'react';
import jwt_decode from 'jwt-decode';

import Admin from './components/Admin';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import NotFound from './components/NotFound';
import OrganismAddForm from './components/OrganismAddForm';
import OrganismUpdateForm from './components/OrganismUpdateForm';
import ParkOrganismAddForm from './components/ParkOrganismAddForm';
import ParkOrganismUpdateForm from './components/ParkOrganismUpdateForm';
import ServerError from './components/ServerError';
import Sightings from './components/Sightings';
import SightingsForm from './components/SightingsForm';
import SightingsUpdateForm from './components/SightingsUpdateForm';
import TrailReviewAddForm from './components/TrailReviewAddForm';
import TrailReviews from './components/TrailReviews';
import TrailReviewUpdateForm from './components/TrailReviewUpdateForm';
import UserPage from './components/Userpage';
import UserContext from './UserContext';

const LOCALSTORAGEKEY = 'findingwildlifeAppToken';


function App() {

  const [user, setUser] = useState(null);

  const login = (token) => {
    const decodedToken = jwt_decode(token);
    console.log(decodedToken);

    localStorage.setItem(LOCALSTORAGEKEY, token);

    const roles = decodedToken.authorities.split(',');

    const user = {
      appUserId: decodedToken.appUserId,
      username: decodedToken.sub,
      roles,
      token,
      hasRole: function(role) {
        return this.roles.includes(role);
      }
    }

    setUser(user);
  }

  const logout = () => {
    localStorage.removeItem(LOCALSTORAGEKEY);
    setUser(null);
  }

  const authManager = {
    user: user ? {...user} : null,
    login,
    logout
  }

  useState(() => {
    const previousSavedToken = localStorage.getItem(LOCALSTORAGEKEY);
    if (previousSavedToken) {
      login(previousSavedToken);
    }
  }, [])
  
  return (
    <div className="App">
      <UserContext.Provider value={authManager}>
        <Router>
          <nav className="navbar navbar-dark navbar-expand-md bg-dark">
            <Link className='navbar-brand' to="/">Finding Wildlife</Link>
            <div className="collapse navbar-collapse" id="navbarText">
              <ul className="navbar-nav mr-auto">
                <li className="nav-item">
                  <Link to="/" className='nav-link'>Home</Link>
                </li>
                <li className="nav-item">
                  <Link to="/sightings" className='nav-link'>Sightings</Link>
                </li>
                <li className="nav-item">
                  <Link to="/trail-reviews" className='nav-link'>Trail Reviews</Link>
                </li>
                {user && user.hasRole("ROLE_ADMIN") ? (
                  <li className="nav-item">
                    <Link to="/admin" className='nav-link'>Admin</Link>
                  </li>
                ) : null}
                {user ? (
                  <>
                  <li>
                    <Link to="/sightings/add" className='nav-link'>Submit</Link>
                  </li>
                    <li className="nav-item">
                      <Link to="/userPage" className='nav-link'>Profile</Link>
                    </li>
                  </>
                ) : (
                  <>
                    <li>
                      <Link to="/login" className='nav-link login-button'>Login</Link>
                    </li>
                    <li>
                      <Link to="/register" className='nav-link register-button'>Register</Link>
                    </li>
                  </>
                )}
              </ul>
              {user && (
                <div>
                  <button className='logout-button' onClick={() => logout()}>Logout</button>
                </div>
              )}
            </div>
          </nav>
        <div className="container">
          <Switch>
            <Route exact path="/login">
              {!user ? <Login /> : <Redirect to="/" />}
            </Route>
            <Route path="/register">
              {!user ? <Register /> : <Redirect to="/" />}
            </Route>
            <Route path="/admin">
              {user && user.hasRole("ROLE_ADMIN") ? <Admin/> : <Redirect to="/" />}
            </Route>
            <Route path="/organism/add">
              {user && user.hasRole("ROLE_ADMIN") ? <OrganismAddForm/> : <Redirect to="/" />}
            </Route>
            <Route path="/organism/update">
              {user && user.hasRole("ROLE_ADMIN") ? <OrganismUpdateForm/> : <Redirect to="/" />}
            </Route>
            <Route path="/park-organism/add">
              {user && user.hasRole("ROLE_ADMIN") ? <ParkOrganismAddForm/> : <Redirect to="/" />}
            </Route>
            <Route path="/park-organism/update">
              {user && user.hasRole("ROLE_ADMIN") ? <ParkOrganismUpdateForm/> : <Redirect to="/" />}
            </Route>
            <Route exact path="/sightings">
              <Sightings/>
            </Route>
            <Route path="/trail-reviews">
              <TrailReviews/>
            </Route>
            <Route path="/trail-review/add">
              {user ? <TrailReviewAddForm/> : <Redirect to="/trail-reviews" />}
            </Route>
            <Route path="/trail-review/update/:editId">
              <TrailReviewUpdateForm/>
            </Route>
            <Route path="/sightings/add">
              {user ? <SightingsForm/> : <Redirect to="/sightings" />}
            </Route>
            <Route path="/sightings/update/:editId">
              {user ? <SightingsUpdateForm/> : <Redirect to="/sightings" />}
            </Route>
            <Route path="/userPage">
              {user ? <UserPage/> : <Redirect to="/" />}
            </Route>
            <Route exact path="/error">
              <ServerError/>
            </Route>
            <Route exact path="/">
              <Home/>
            </Route>
            <Route path="*">
              <NotFound/>
            </Route>
          </Switch>
        </div>
        </Router>
        <footer className="bg-dark text-center text-white footer">
        <div className="text-center p-3 copyright">
          <p>© 2022 Copyright: FindingWildlife.com</p>
        </div>
      </footer>
      </UserContext.Provider>
    </div>
  ); 
}

export default App;