import Link from "next/link";
import types from "../types";
import ProfilePic from "./ProfilePic";
import {TopCreator as TopCreatorType} from "../types";

export default function TopCreator(props) {
  return (
    <>
      <Link key={props.id} className="m-1 link" href={`/profile?id=${props.id}`}>
        <div
          className="card text-white d-flex flex-row p-2 creator-card align-items-center"
          id="none_shadow_creator"
        >
          <div className="img-container">
            <ProfilePic {...props}></ProfilePic>
          </div>
          <div className="mx-2 text-ellipsis-1">{props.nameOrEmail}</div>
        </div>
      </Link>
    </>
  );
}

TopCreator.propTypes = TopCreatorType


