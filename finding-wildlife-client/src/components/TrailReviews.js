import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import UserContext from "../UserContext";
import ParkSelection from "./ParkSelection";
import Trail from "./Trail";
import TrailReview from "./TrailReview";


function TrailReviews(){

    const history = useHistory();
    const [parks, setParks] = useState([]);
    const [trails, setTrails] = useState([]);
    const [trailReviews, setTrailReviews] = useState([]);

    const authManager = useContext(UserContext);

    useEffect(() => {
      fetch('http://localhost:8080/finding-wildlife/park')
      .then(response => {
        if(response.status === 200) {
          return response.json();
        }
        return Promise.reject('Something Went Wrong!');
      })
      .then(data => {
        setParks(data);
      })
      .catch(error => history.push('/error', {errorMessage: error}));
    }, []);

    function handleParkChange(event){
      fetch(`http://localhost:8080/finding-wildlife/park/${event.target.value}`)
      .then(response => {
        if(response.status === 200) {
          return response.json();
        }
        return Promise.reject('Something Went Wrong');
      })
      .then(data => {
        setTrails([...data.trails])
      })
      .catch(error => history.push('/error', {errorMessage: error}));
    }

    function handleTrailChange(event){
      fetch(`http://localhost:8080/finding-wildlife/trail-review/trailId/${event.target.value}`)
        .then(response => {
          switch(response.status) {
            case 200:
              return response.json();
            case 404:
              history.push('/not-found')
              break;
            default:
              return Promise.reject('Something Went Wrong!');
          }
        })
        .then(body => {
          if(body) {
            setTrailReviews(body);
          }
        })
        .catch(err => history.push('/error', {errorMessage: err}));
    }

    function handleAddReview(){
      history.push('/trail-review/add')
    }

    function deleteReview(trailReviewId){
      const filteredTrailReviews = trailReviews.filter(review => review.trailReviewId !== trailReviewId);
      setTrailReviews(filteredTrailReviews);
    }



    return(<>
    <div className="trail-reviews-container">
    <h2>Trail Reviews</h2>
    <form className="trail-review-form">
        <div className='form-group'>
          <select className="form-control-sm" onChange={handleParkChange}>
            <option value="1" >Select National Park</option>
              {parks.map(park => <ParkSelection key={park.parkId} park={park}/>)}
            </select>
          </div>
            <div className='form-group'>
              <select className="form-control-sm mt-2" onChange={handleTrailChange}>
              <option value="0" >Select a Trail</option>
                {trails && trails.map(t => {
                  return <Trail key={t.trailId} trail={t}/>})}
              </select>
            </div>
        </form>
        {authManager.user && <button type="button" className="btn btn-success mt-2 mb-4" onClick={handleAddReview}>Add Trail Review</button>}
        <div className="review-container">
        {trailReviews.map(review => <TrailReview key={review.trailReviewId} review={review} handleDelete={deleteReview}/>)}
        </div>
      </div>
    </>);
}

export default TrailReviews;
