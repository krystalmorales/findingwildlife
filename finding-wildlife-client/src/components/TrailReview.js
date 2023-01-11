import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import UserContext from "../UserContext";
import StarRating from "./StarRating";

function TrailReview( {review, handleDelete} ){

    const history = useHistory();
    const authManager = useContext(UserContext);

    // const [owner, setOwner] = useState();

    //   useEffect(() => {
    //     fetch(`http/localhost:8080/${review.appUserId}`)
    //     .then(response => {
    //       if(response.status === 200) {
    //         return response.json();
    //       }
    //       return Promise.reject('Something Went Wrong!');
    //     })
    //     .then(data => {
    //         console.log(data)
    //         setOwner(data);
    //     })
    //     .catch(error => history.push('/error', {errorMessage: error}));
    // }, []);



    function confirmDelete(){
        if(window.confirm("Are you sure you want to Delete this Trail Review?")){
            handleDeleteClick();
        }
    }

    function handleUpdate(){
        history.push(`/trail-review/update/${review.trailReviewId}`)
    }

    function handleDeleteClick(){
        const init = {
            method: 'DELETE',
            headers: {
                Authorization: `Bearer ${authManager.user.token}`
            }
        };

        fetch(`http://localhost:8080/finding-wildlife/trail-review/${review.trailReviewId}/${review.appUserId}`, init)
        .then(response => {
            switch(response.status) {
                case 204:
                    return null;
                case 404:
                    history.push('./not-found')
                    break;
                default:
                    return Promise.reject('Something Went Wrong');
            }
        })
        .then(response => {
            if(!response) {
                handleDelete(review.trailReviewId)
            }
            else{
                console.log(response)
            }
        })
        .catch(error => history.push('./error', {errorMessage: error}))
    }

    return(<>
        <div className="card bg-light mb-3">
            <div className="card-header"></div>
            <div className="card-body">
                <h6 className="card-title">Rating: {review.rating} <StarRating rating={review.rating}/></h6>
                <h6 className="card-title">Difficulty: {review.difficulty} <StarRating rating={review.difficulty}/></h6>
                <p className="card-text">{review.comments}</p>
                {/* <button type="button" className="btn btn-primary mr-3" onClick={handleUpdate}>Edit</button> */}
                {authManager.user && authManager.user.hasRole('ROLE_ADMIN') && <button type="button" className="btn btn-danger trail-review-delete" onClick={confirmDelete}>Delete</button>}
            </div>
        </div>
    </>);
}

export default TrailReview;