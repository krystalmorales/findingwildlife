import { useContext, useEffect, useState } from 'react';
import { useHistory } from "react-router-dom";
import UserContext from "../UserContext";
import Sighting from "./Sighting";
import Review from './Review';

function UserPage() {

    const [sightings, setSightings] = useState([]);
    const [reviews, setReviews] = useState([]);
    const [trails, setTrails] = useState([]);

    const history = useHistory();
    const authManager = useContext(UserContext);

        useEffect(() => {
            const init = {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${authManager.user.token}`
                }
            }
            fetch(`http://localhost:8080/finding-wildlife/trail-review/userId/${authManager.user.appUserId}`, init)
            .then(resp => {
                if (resp.status === 200) {
                  return resp.json();
                }
                return Promise.reject('Something went wrong.');
              })
              .then(data => {
                setReviews(data);
              })
              .catch(err => history.push('/error', {errorMessage: err}));

            fetch(`http://localhost:8080/finding-wildlife/sighting/userId/${authManager.user.appUserId}`, init)
            .then(resp => {
                if (resp.status === 200) {
                return resp.json();
                }
                return Promise.reject('Something went wrong.');
            })
            .then(data => {
                setSightings(data);
            })
            .catch(err => history.push('/error', {errorMessage: err}));

            fetch(`http://localhost:8080/finding-wildlife/trail`)
            .then(response => {
              if(response.status === 200) {
                return response.json();
              }
              return Promise.reject('Something Went Wrong!');
            })
            .then(data => {
              setTrails(data);
            })
            .catch(error => history.push('/error', {errorMessage: error}));
        },[])

        function deleteSighting(sightingId){
            const filteredSightings = sightings.filter(sighting => sighting.sightingId !== sightingId);
            setSightings(filteredSightings);
        }

        function deleteReview(trailReviewId){
            const filteredTrailReviews = reviews.filter(review => review.trailReviewId !== trailReviewId);
            setReviews(filteredTrailReviews);
          }

    return (
        <div className="row userpage-container">
            <h1 className="profile-header">{authManager.user.username}'s Profile</h1>
            <h2 className="user-s-header">Sightings ({sightings.length})</h2>
            <div className='empty-box'></div>
            <h2 className="user-tr-header">Trail Reviews ({reviews.length})</h2>
        <div className="col-md-6 user-sighting-container">
            <div className="table-responsive">
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th className="col-md-1">Date</th>
                            <th className="col-md-2">Time</th>
                            <th className="col-md-3 sighting-comments">Comments</th>
                            <th className="col-md-6">National Park</th>
                            <th className="col-md-7">Organism</th>
                        </tr>
                    </thead>
                    <tbody>
                        {sightings.map(sighting => <Sighting key={sighting.sightingId} sighting={sighting} handleDelete={deleteSighting} />)}
                    </tbody>
                </table>
            </div>
        </div>
            <div className="col-md-6 user-sighting-container">
                <div className="table-responsive">
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th className="col-md-1">Trail Rating</th>
                                <th className="col-md-2">Trail Difficulty</th>
                                <th className="col-md-3">Comments</th>
                                <th className="col-md-4">Trail</th>
                            </tr>
                        </thead>
                        <tbody>
                            {reviews.map(review => <Review key={review.trailReviewId} review={review} trails ={trails} handleDelete={deleteReview}/>)}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>    
    
    );
}

export default UserPage;