import {PersonaViewModel} from "../persona/persona-view-model";

export interface BidViewModel {
  id: number;
  offerId: number;
  userEmail: string;
  persona?: PersonaViewModel;
  freeProductSample: boolean;
  acceptInitialRequirements: boolean;
  compensation: string;
}
