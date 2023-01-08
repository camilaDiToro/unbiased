import Link from "next/link";
import PropTypes from "prop-types";
import types from "../types";

export default function TopCreator(props) {
  return (
    <>
      <Link key={props.id} className="m-1 link" href={`/profile/${props.id}`}>
        <div
          className="card text-white d-flex flex-row p-2 creator-card align-items-center"
          id="none_shadow_creator"
        >
          <div className="img-container">
            <img
              className="rounded-circle object-fit-cover mr-1"
              src="/img/profile-image.png"
              alt=""
            />
          </div>
          <div className="mx-2 text-ellipsis-1">{props.nameOrEmail}</div>
        </div>
      </Link>
    </>
  );
}

TopCreator.propTypes = types.TopCreator;
